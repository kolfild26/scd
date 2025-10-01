package ru.sportmaster.scd.service.airflow;

import static ru.sportmaster.scd.consts.ParamNames.AIRFLOW_AUTH_TOKEN;
import static ru.sportmaster.scd.consts.ParamNames.P_DAG_ID;
import static ru.sportmaster.scd.consts.ParamNames.P_ID_BUSINESS_TMA;
import static ru.sportmaster.scd.consts.ParamNames.P_ID_PARTITION_DIV_TMA;
import static ru.sportmaster.scd.utils.ConvertUtil.getLongValue;
import static ru.sportmaster.scd.utils.EnvironmentUtils.getEnvironmentValues;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import ru.sportmaster.scd.exceptions.AirflowException;
import ru.sportmaster.scd.service.credentials.ICredentialsService;

@Slf4j
@RequiredArgsConstructor
abstract class AirflowService implements IAirflowService {
    @Value("${scd.airflow.uri}")
    protected String uri;

    protected final ICredentialsService credentialsService;
    protected final AirflowWebService airflowWebService;

    protected final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void finishAlgorithm(@NonNull Long algorithmId,
                                @NonNull Map<String, Object> params) {
        try {
            log.debug("Информация Airflow по алгоритму: {}", algorithmId);
            Executors
                .newSingleThreadExecutor()
                .execute(() -> finishAlgorithmTask(algorithmId, params, getEnvironmentValues()));
        } catch (Exception e) {
            log.error("Ошибка информирования Airflow {} по алгоритму: {}", e.getMessage(), algorithmId, e);
        }
    }

    protected abstract void finishAlgorithmTask(@NonNull Long algorithmId,
                                                @NonNull Map<String, Object> params,
                                                @NonNull Map<String, String> environment);

    protected String getUri(@NonNull Map<String, Object> params) {
        return uri.formatted(getDagId(params));
    }

    protected String getDagId(@NonNull Map<String, Object> params) {
        return
            (String) Optional.ofNullable(params.get(P_DAG_ID))
                .orElseThrow(
                    () -> new AirflowException("Идентификатор DAG не определен.")
                );
    }

    protected Long getPartitionId(@NonNull Map<String, Object> params) {
        return getLongValue(params.get(P_ID_PARTITION_DIV_TMA));
    }

    protected Long getBusinessId(@NonNull Map<String, Object> params) {
        return getLongValue(params.get(P_ID_BUSINESS_TMA));
    }

    protected String getAirflowAuthToken(Map<String, Object> params) {
        return (String) params.get(AIRFLOW_AUTH_TOKEN);
    }

    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected static class RunDagConfRequest {
        @JsonProperty("alg_id")
        private Long algorithmId;
        @JsonProperty("java_trigger_flag")
        @Builder.Default
        private Integer javaTriggerFlag = 1;
        @JsonProperty("run_for_partition")
        private Long partition;
        @JsonProperty("run_for_business")
        private Long business;
        @JsonProperty("token")
        private String airflowAuthToken;
    }
}
