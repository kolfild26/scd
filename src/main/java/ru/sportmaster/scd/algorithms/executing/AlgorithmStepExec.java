package ru.sportmaster.scd.algorithms.executing;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;
import ru.sportmaster.scd.algorithms.IExecutor;
import ru.sportmaster.scd.entity.algorithms.definitions.AlgorithmStepDefinition;
import ru.sportmaster.scd.entity.algorithms.execution.AlgorithmStep;

@Getter
public class AlgorithmStepExec {
    private final AlgorithmStepDefinition stepDefinition;
    private final IExecutor executor;

    @Setter
    private AlgorithmStep step;

    public AlgorithmStepExec(@NonNull AlgorithmStepDefinition stepDefinition,
                             @NonNull IExecutor executor) {
        this.stepDefinition = stepDefinition;
        this.executor = executor;
        this.step = null;
    }
}
