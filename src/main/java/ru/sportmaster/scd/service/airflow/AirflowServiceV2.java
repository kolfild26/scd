package ru.sportmaster.scd.service.airflow;

import static java.util.Objects.isNull;
import static ru.sportmaster.scd.consts.ParamCredentials.AIRFLOW_CREDENTIAL_NAME;
import static ru.sportmaster.scd.consts.ParamNames.AIRFLOW_JWT_AUTH_TOKEN;
import static ru.sportmaster.scd.consts.ParamNames.DAG_ID_RESPONSE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.DAG_PAUSED_NAME;
import static ru.sportmaster.scd.consts.ParamNames.DAG_RUN_ID_RESPONSE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.ERROR_AUTH_RESPONSE_NAME;
import static ru.sportmaster.scd.utils.EnvironmentUtils.restoreEnvironment;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.sportmaster.scd.exceptions.AirflowException;
import ru.sportmaster.scd.service.credentials.ICredentialsService;

@Slf4j
@Service
@Profile("prod | test")
@ConditionalOnExpression("'${scd.airflow.version}'.equals('/v2')")
public class AirflowServiceV2 extends AirflowService {
    private final String uriInfo;

    private final AccessToken accessToken;

    public AirflowServiceV2(@Value("${scd.airflow.uri-auth}") String uriAuth,
                            @Value("${scd.airflow.uri-info}") String uriInfo,
                            ICredentialsService credentialsService,
                            AirflowWebService airflowWebService) {
        super(credentialsService, airflowWebService);
        this.uriInfo = uriInfo;
        this.accessToken =
            new AccessToken(
                airflowWebService,
                credentialsService,
                mapper,
                uriAuth
            );
    }

    @PostConstruct
    public void init() {
        prepareAuthorizationToken();
    }

    private void prepareAuthorizationToken() {
        log.debug("Airflow API V2");
        try {
            accessToken.getToken();
        } catch (Exception e) {
            log.error("Ошибка авторизации Airflow {}", e.getMessage(), e);
        }
    }

    @Override
    protected void finishAlgorithmTask(@NonNull Long algorithmId,
                                       @NonNull Map<String, Object> params,
                                       @NonNull Map<String, String> environment) {
        restoreEnvironment(environment);
        try {
            log.debug("Получение информации Airflow по DAG для алгоритма: {}", algorithmId);
            var airflowResponse =
                airflowWebService.getWebClient()
                    .get()
                    .uri(getUriInfo(params))
                    .headers(
                        headers -> {
                            headers.setBearerAuth(accessToken.getToken());
                            headers.setContentType(MediaType.APPLICATION_JSON);
                        }
                    )
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            var jsonNode = checkErrors(mapper.readTree(airflowResponse));
            log.debug(
                "Информация по DAG {} получена.",
                jsonNode.get(DAG_ID_RESPONSE_NAME).textValue()
            );

            if (jsonNode.get(DAG_PAUSED_NAME).asBoolean()) {
                throw new AirflowException("DAG уже запущен.");
            }

            log.debug("Передача информации Airflow по алгоритму: {}", algorithmId);
            airflowResponse =
                airflowWebService.getWebClient()
                    .post()
                    .uri(getUri(params))
                    .headers(
                        headers -> {
                            headers.setBearerAuth(accessToken.getToken());
                            headers.setContentType(MediaType.APPLICATION_JSON);
                        }
                    )
                    .bodyValue(getBodyV2(algorithmId, params))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            jsonNode = checkErrors(mapper.readTree(airflowResponse));
            log.debug(
                "DAG {} запущен с идентификатором запуска {}",
                jsonNode.get(DAG_ID_RESPONSE_NAME).textValue(),
                jsonNode.get(DAG_RUN_ID_RESPONSE_NAME).textValue()
            );
        } catch (Exception e) {
            log.error("Ошибка передачи информации Airflow {} по алгоритму: {}", e.getMessage(), algorithmId, e);
        }
    }

    private String getUriInfo(@NonNull Map<String, Object> params) {
        return uriInfo.formatted(getDagId(params));
    }

    private Object getBodyV2(@NonNull Long algorithmId,
                             @NonNull Map<String, Object> params) {
        return
            RunDagRequestV2.builder()
                .logicalDate(null)
                .conf(
                    RunDagConfRequest.builder()
                        .algorithmId(algorithmId)
                        .partition(getPartitionId(params))
                        .business(getBusinessId(params))
                        .airflowAuthToken(getAirflowAuthToken(params))
                        .build()
                )
                .build();
    }

    private JsonNode checkErrors(JsonNode response) {
        if (response.has(ERROR_AUTH_RESPONSE_NAME)) {
            throw new AirflowException(response.asText());
        }
        return response;
    }

    @RequiredArgsConstructor
    private static class AccessToken {
        private final AirflowWebService airflowWebService;
        private final ICredentialsService credentialsService;
        private final ObjectMapper mapper;
        private final String uriAuth;

        private String token = null;

        public synchronized String getToken() {
            if (isTokenExpired()) {
                token = refreshToken();
            }
            return token;
        }

        private boolean isTokenExpired() {
            return
                isNull(token)
                    || token.isBlank()
                    || JWT.decode(token).getExpiresAt().before(new Date());
        }

        private String refreshToken() {
            log.debug("Обновление JWT токена Airflow");
            try {
                var credentials =
                    Optional.ofNullable(credentialsService.getCredential(AIRFLOW_CREDENTIAL_NAME))
                        .orElseThrow(
                            () -> new AirflowException(
                                "Параметры входа %s не найдены.".formatted(AIRFLOW_CREDENTIAL_NAME)
                            )
                        );
                var credentialsValue = credentials.getValue();
                var airflowResponse =
                    airflowWebService.getWebClientAuth()
                        .post()
                        .uri(uriAuth)
                        .headers(headers -> headers.setContentType(MediaType.APPLICATION_JSON))
                        .bodyValue(
                            AuthRequest.builder()
                                .userName(credentialsValue.getLogin())
                                .password(credentialsValue.getPassword())
                                .build()
                        )
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();
                var jsonNode = checkAuthErrors(mapper.readTree(airflowResponse));
                log.debug("JWT токен Airflow получен");
                return jsonNode.get(AIRFLOW_JWT_AUTH_TOKEN).textValue();
            } catch (Exception e) {
                throw new AirflowException(e.getMessage(), e);
            }
        }

        private JsonNode checkAuthErrors(JsonNode response) {
            if (response.has(ERROR_AUTH_RESPONSE_NAME)) {
                throw new AirflowException(response.asText());
            }
            return response;
        }

        @Builder
        @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
        private static class AuthRequest {
            @JsonProperty("username")
            private String userName;
            private String password;
        }

    }

    @Builder
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    private static class RunDagRequestV2 {
        @JsonProperty("logical_date")
        private String logicalDate;
        private RunDagConfRequest conf;
    }
}
