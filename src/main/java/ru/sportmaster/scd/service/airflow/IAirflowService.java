package ru.sportmaster.scd.service.airflow;

import java.util.Map;
import org.springframework.lang.NonNull;

/**
 * Сервис обратной связи с AirFlow.
 */
public interface IAirflowService {
    void finishAlgorithm(@NonNull Long algorithmId,
                         @NonNull Map<String, Object> params);
}
