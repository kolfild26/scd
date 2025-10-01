package ru.sportmaster.scd.repository.algorithms.execution;

import java.util.Map;
import org.springframework.lang.NonNull;
import ru.sportmaster.scd.entity.algorithms.definitions.AlgorithmStepDefinition;
import ru.sportmaster.scd.entity.algorithms.execution.AlgorithmStage;
import ru.sportmaster.scd.entity.algorithms.execution.AlgorithmStep;
import ru.sportmaster.scd.repository.AbstractRepository;

public interface AlgorithmStepRepository extends AbstractRepository<AlgorithmStep, Long> {
    AlgorithmStep create(@NonNull AlgorithmStage algorithmStage,
                         @NonNull AlgorithmStepDefinition algorithmStepDefinition,
                         @NonNull Map<String, Object> params);

    void finish(@NonNull Long algorithmStepId,
                @NonNull Map<String, Object> params);
}
