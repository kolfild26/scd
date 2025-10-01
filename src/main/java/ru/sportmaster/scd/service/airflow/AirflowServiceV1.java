package ru.sportmaster.scd.service.airflow;

import static ru.sportmaster.scd.consts.ParamCredentials.AIRFLOW_CREDENTIAL_NAME;
import static ru.sportmaster.scd.consts.ParamNames.DAG_ID_RESPONSE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.DAG_RUN_ID_RESPONSE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.ERROR_TYPE_RESPONSE_NAME;
import static ru.sportmaster.scd.utils.EnvironmentUtils.restoreEnvironment;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.PostConstruct;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;
import ru.sportmaster.scd.exceptions.AirflowException;
import ru.sportmaster.scd.service.credentials.ICredentialsService;

@Slf4j
@Service
@Profile("prod | test")
@ConditionalOnExpression("'${scd.airflow.version}'.equals('/v1')")
public class AirflowServiceV1 extends AirflowService {
    private final AtomicReference<String> authorizationValue = new AtomicReference<>();

    public AirflowServiceV1(ICredentialsService credentialsService,
                            AirflowWebService airflowWebService) {
        super(credentialsService, airflowWebService);
    }

    @PostConstruct
    public void init() {
        log.debug("Airflow API V1");
        prepareAuthorizationValue();
    }

    private void prepareAuthorizationValue() {
        var credentials =
            Optional.ofNullable(credentialsService.getCredential(AIRFLOW_CREDENTIAL_NAME))
                .orElseThrow(
                    () -> new AirflowException("Параметры входа %s не найдены.".formatted(AIRFLOW_CREDENTIAL_NAME))
                );
        var credentialsValue = credentials.getValue();
        authorizationValue.set(
            Base64.encodeBase64String(
                "%s:%s"
                    .formatted(
                        credentialsValue.getLogin(),
                        credentialsValue.getPassword()
                    )
                    .getBytes())
        );
        log.debug("Параметры авторизации в Airflow получены.");
    }

    @Override
    protected void finishAlgorithmTask(@NonNull Long algorithmId,
                                       @NonNull Map<String, Object> params,
                                       @NonNull Map<String, String> environment) {
        restoreEnvironment(environment);
        log.debug("Передача информации Airflow по алгоритму: {}", algorithmId);
        try {
            var airflowResponse =
                airflowWebService.getWebClient(new OneRetryAuthExchangeFilterFunction(this))
                    .post()
                    .uri(getUri(params))
                    .bodyValue(getBodyV1(algorithmId, params))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            var jsonNode = checkErrors(mapper.readTree(airflowResponse));
            log.debug(
                "DAG {} запущен с идентификатором запуска {}",
                jsonNode.get(DAG_ID_RESPONSE_NAME).textValue(),
                jsonNode.get(DAG_RUN_ID_RESPONSE_NAME).textValue()
            );
        } catch (Exception e) {
            log.error("Ошибка передачи информации Airflow {} по алгоритму: {}", e.getMessage(), algorithmId, e);
        }
    }

    private Object getBodyV1(@NonNull Long algorithmId,
                             @NonNull Map<String, Object> params) {
        return
            RunDagRequestV1.builder()
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
        if (response.has(ERROR_TYPE_RESPONSE_NAME)) {
            throw new AirflowException(response.asText());
        }
        return response;
    }

    private record OneRetryAuthExchangeFilterFunction(AirflowServiceV1 airflowService)
        implements ExchangeFilterFunction {
        @Override
        public @NotNull Mono<ClientResponse> filter(@NotNull ClientRequest request,
                                                    ExchangeFunction next) {
            ClientRequest authenticatedRequest = applyAuthentication(request);
            return next.exchange(authenticatedRequest)
                .flatMap(response -> {
                    if (HttpStatus.UNAUTHORIZED.equals(response.statusCode())) {
                        airflowService.prepareAuthorizationValue();
                        ClientRequest refreshedAuthenticatedTokenRequest = applyAuthentication(request);
                        return next.exchange(refreshedAuthenticatedTokenRequest);
                    }
                    return Mono.just(response);
                });
        }

        private ClientRequest applyAuthentication(ClientRequest request) {
            return ClientRequest.from(request)
                .headers(headers -> {
                        headers.setBasicAuth(airflowService.authorizationValue.get());
                        headers.setContentType(MediaType.APPLICATION_JSON);
                    }
                )
                .build();
        }
    }

    @Builder
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    private static class RunDagRequestV1 {
        private RunDagConfRequest conf;
    }
}
