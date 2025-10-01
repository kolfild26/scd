package ru.sportmaster.scd.repository.algorithms.execution;

import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.lang.NonNull;
import ru.sportmaster.scd.entity.algorithms.definitions.AlgorithmDefinition;
import ru.sportmaster.scd.entity.algorithms.execution.Algorithm;
import ru.sportmaster.scd.repository.AbstractRepository;

public interface AlgorithmRepository extends AbstractRepository<Algorithm, Long> {
    Algorithm create(@NonNull AlgorithmDefinition algorithmDefinition,
                     @NonNull Map<String, Object> params);

    Algorithm start(@NonNull Long algorithmId);

    void finish(@NonNull Long algorithmId,
                @NonNull Map<String, Object> params);

    void cancel(@NonNull Long algorithmId,
                @NonNull LocalDateTime cancelTime);

    Algorithm getLastAlgorithm(Long algorithmDefId, boolean isFinished);

    Long getAlgDuration(Long algorithmDefId);
}
