package ru.sportmaster.scd.algorithms.executing;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;
import ru.sportmaster.scd.entity.algorithms.definitions.AlgorithmStageDefinition;
import ru.sportmaster.scd.entity.algorithms.execution.AlgorithmStage;

@Getter
public class AlgorithmStageExec {
    private final AlgorithmStageDefinition stageDefinition;
    private final List<AlgorithmStepExec> stepExecs;

    @Setter
    private AlgorithmStage stage;


    public AlgorithmStageExec(@NonNull AlgorithmStageDefinition stageDefinition,
                              @NonNull List<AlgorithmStepExec> stepExecs) {
        this.stageDefinition = stageDefinition;
        this.stepExecs = stepExecs;
    }
}
