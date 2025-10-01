package ru.sportmaster.scd.algorithms.information;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.NonNull;
import ru.sportmaster.scd.entity.algorithms.definitions.AlgorithmStageDefinition;
import ru.sportmaster.scd.entity.algorithms.execution.AlgorithmStage;

@Getter
@EqualsAndHashCode(callSuper = true, exclude = "steps")
public class AlgorithmStageInfo extends AlgorithmStepInfo {
    @Schema(
        description = "Шаги алгоритма",
        name = "steps")
    private final List<AlgorithmStepInfo> steps;

    private AlgorithmStageInfo(@NonNull AlgorithmInfoParam algorithmInfoParam) {
        super(algorithmInfoParam);
        this.steps = new ArrayList<>();
    }

    public static @NotNull AlgorithmStageInfo createAlgorithmStageInfo(
        AlgorithmStage algorithmStage,
        AlgorithmStageDefinition algorithmStageDefinition) {
        return new AlgorithmStageInfo(AlgorithmInfoParam.create(algorithmStageDefinition, algorithmStage));
    }
}
