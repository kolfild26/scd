package ru.sportmaster.scd.credentials;

import static ru.sportmaster.scd.consts.ParamCredentials.DATA_NAME;

import java.util.Optional;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import ru.sportmaster.scd.entity.User;
import ru.sportmaster.scd.exceptions.VaultException;

/**
 * Общая реализация параметров входа.
 */
@Slf4j
public class Credentials implements ICredentials {
    @Getter
    private final String name;
    private final CredentialsConfig config;

    public Credentials(@NonNull String name,
                       @NonNull CredentialsConfig config) {
        this.name = name;
        this.config = config;
    }

    @Override
    public User getValue() {
        try {
            var token =
                Optional.ofNullable(config.getToken())
                    .orElseThrow(() -> new VaultException("Отсутствует токен"));
            var vaultSecretRoot = token.getSecret();
            var vaultSecret =
                Optional.ofNullable(vaultSecretRoot)
                    .map(jsonNode -> jsonNode.get(DATA_NAME))
                    .orElseThrow(() -> new VaultException("Отсутствует ключ %s".formatted(DATA_NAME)));
            log.debug("Получены параметры входа для {}", name);
            return
                User.builder()
                    .login(vaultSecret.get(config.getUsernameKeyName()).textValue())
                    .password(vaultSecret.get(config.getPasswordKeyName()).textValue())
                    .build();
        } catch (Exception e) {
            log.error("Ошибка получения параметров входа {}: {}", name, e.getMessage(), e);
        }
        return User.builder().build();
    }
}
