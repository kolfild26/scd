package ru.sportmaster.scd.tokens;

import static ru.sportmaster.scd.consts.ParamCredentials.AUTH_RESPONSE_NAME;
import static ru.sportmaster.scd.consts.ParamCredentials.CLIENT_TOKEN_RESPONSE_NAME;
import static ru.sportmaster.scd.consts.ParamCredentials.ERROR_RESPONSE_NAME;
import static ru.sportmaster.scd.consts.ParamCredentials.LEASE_DURATION_NAME;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import ru.sportmaster.scd.exceptions.VaultException;
import ru.sportmaster.scd.service.tokens.VaultService;

/**
 * Общая реализация для токенов Vault.
 */
@Slf4j
public class AbstractVaultToken implements IToken {
    @Getter
    private final String name;
    private final TokenConfig config;
    private final AtomicReference<String> token;
    private final ObjectMapper mapper = new ObjectMapper();
    private final VaultService vaultService;

    public AbstractVaultToken(@NonNull String name,
                              @NonNull TokenConfig config,
                              VaultService vaultService) {
        this.name = name;
        this.config = config;
        this.vaultService = vaultService;
        this.token = new AtomicReference<>();
        loadToken();
    }

    @Override
    public JsonNode getSecret() throws JsonProcessingException {
        String vaultResponse =
            vaultService.getWebClient()
                .get()
                .uri(config.getVaultPath())
                .header("X-Vault-Token", token.get())
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return checkErrors(mapper.readTree(vaultResponse));
    }

    private JsonNode checkErrors(JsonNode response) {
        if (response.has(ERROR_RESPONSE_NAME)) {
            throw new VaultException(response.get(ERROR_RESPONSE_NAME).asText());
        }
        return response;
    }

    @Override
    public void loadToken() {
        try {
            token.set(vaultService.loadTokenFromFile(config.getFileName()));
            log.debug("{} токен успешно загружен.", name);
        } catch (Exception e) {
            log.error("Ошибка чтения токена {}: {}", name, e.getMessage(), e);
        }
    }

    @Override
    public boolean isChangeToken() {
        try {
            if (getTokenDayExpired() <= config.getDayBeforeExpired()) {
                storeToken(getNewToken());
                return true;
            }
        } catch (Exception e) {
            log.error("Ошибка обновления токена {}: {}", name, e.getMessage(), e);
        }
        return false;
    }

    private long getTokenDayExpired() {
        long leaseDuration = config.getDayBeforeExpired() + 1;
        try {
            long secondsToDay = 24L * 60 * 60;
            leaseDuration = getSecret().get(LEASE_DURATION_NAME).longValue() / secondsToDay;
        } catch (Exception e) {
            log.error("Ошибка определения срока истечения токена {}: {}", name, e.getMessage(), e);
        }
        return leaseDuration;
    }

    private String getNewToken() throws JsonProcessingException {
        String vaultResponse =
            vaultService.getWebClient()
                .post()
                .uri(config.getCreateOrphanPath())
                .header("X-Vault-Token", token.get())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(
                    CreateTokenRequest.builder()
                        .policies(config.getVaultPolices())
                        .ttl(config.getVaultTtl())
                        .build()
                )
                .retrieve()
                .bodyToMono(String.class)
                .block();
        var response = checkErrors(mapper.readTree(vaultResponse));
        return
            Optional.ofNullable(response.get(AUTH_RESPONSE_NAME))
                .map(jsonNode -> jsonNode.get(CLIENT_TOKEN_RESPONSE_NAME))
                .map(JsonNode::textValue)
                .orElseThrow(
                    () ->
                        new VaultException(
                            String.format("%s. Ошибка определения нового токена. Ответ: %s", name, response)
                        )
                );
    }

    private void storeToken(@NonNull String token) throws IOException {
        vaultService.saveTokenToFile(config.getFileName(), token);
        log.debug("{} токен успешно сохранен.", name);
    }

    @Builder
    private static class CreateTokenRequest {
        private String policies;
        private String ttl;
        @JsonProperty("no_default_policy")
        @Builder.Default
        private boolean noDefaultPolicy = false;
    }
}
