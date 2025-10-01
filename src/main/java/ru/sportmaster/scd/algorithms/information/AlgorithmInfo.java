package ru.sportmaster.scd.algorithms.information;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.NonNull;
import ru.sportmaster.scd.entity.algorithms.definitions.AlgorithmDefinition;
import ru.sportmaster.scd.entity.algorithms.execution.Algorithm;

@Getter
@EqualsAndHashCode(callSuper = true, exclude = "stages")
public class AlgorithmInfo extends AlgorithmStepInfo {
    @Schema(
        description = "Этапы алгоритма",
        name = "stages")
    private final List<AlgorithmStageInfo> stages;

    private AlgorithmInfo(@NonNull AlgorithmInfoParam algorithmInfoParam) {
        super(algorithmInfoParam);
        this.stages = new ArrayList<>();
    }

    public static @NotNull AlgorithmInfo createAlgorithmInfo(Algorithm algorithm,
                                                             AlgorithmDefinition algorithmDefinition) {
        return new AlgorithmInfo(AlgorithmInfoParam.create(algorithmDefinition, algorithm));
    }
}
