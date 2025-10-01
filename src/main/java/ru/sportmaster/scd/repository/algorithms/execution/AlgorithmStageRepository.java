package ru.sportmaster.scd.repository.algorithms.execution;

import java.util.Map;
import org.springframework.lang.NonNull;
import ru.sportmaster.scd.entity.algorithms.definitions.AlgorithmStageDefinition;
import ru.sportmaster.scd.entity.algorithms.execution.Algorithm;
import ru.sportmaster.scd.entity.algorithms.execution.AlgorithmStage;
import ru.sportmaster.scd.repository.AbstractRepository;

public interface AlgorithmStageRepository extends AbstractRepository<AlgorithmStage, Long> {
    AlgorithmStage create(@NonNull Algorithm algorithm,
                          @NonNull AlgorithmStageDefinition algorithmStageDefinition,
                          @NonNull Map<String, Object> params);

    void finish(@NonNull Long algorithmStageId,
                @NonNull Map<String, Object> params);
}
