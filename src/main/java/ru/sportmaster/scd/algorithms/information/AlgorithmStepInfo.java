package ru.sportmaster.scd.algorithms.information;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.NonNull;
import ru.sportmaster.scd.algorithms.AlgComponentStatus;
import ru.sportmaster.scd.algorithms.IAlgComponent;
import ru.sportmaster.scd.algorithms.IAlgComponentDefine;
import ru.sportmaster.scd.entity.algorithms.definitions.AlgorithmStepDefinition;
import ru.sportmaster.scd.entity.algorithms.execution.AlgorithmStep;

@Getter
@EqualsAndHashCode
public class AlgorithmStepInfo implements IAlgComponent {
    @Schema(
        description = "Идентификатор",
        name = "id",
        requiredMode = Schema.RequiredMode.REQUIRED,
        example = "1")
    protected final Long id;
    @Schema(
        description = "Идентификатор описания",
        name = "definitionId",
        requiredMode = Schema.RequiredMode.REQUIRED,
        example = "1")
    protected final Long definitionId;
    @Schema(
        description = "Имя",
        name = "name",
        requiredMode = Schema.RequiredMode.REQUIRED,
        example = "STEP_OR_STAGE_OR_ALGORITHM")
    protected final String name;
    @Schema(
        description = "Описание",
        name = "description",
        example = "Описание компонента алгоритма (шаг/этап/алгоритм)")
    protected final String description;
    @Schema(
        description = "Порядок",
        name = "order",
        requiredMode = Schema.RequiredMode.REQUIRED,
        example = "0")
    protected final Integer order;
    @Schema(
        description = "Время начала",
        name = "startTime",
        implementation = String.class,
        example = "2022-10-29T12:00:00")
    protected final LocalDateTime startTime;
    @Schema(
        description = "Время окончания",
        name = "endTime",
        implementation = String.class,
        example = "2022-10-29T12:01:59")
    protected final LocalDateTime endTime;
    @Schema(
        description = "Время отмены",
        name = "cancelTime",
        implementation = String.class,
        example = "2022-10-29T12:01:59")
    protected final LocalDateTime cancelTime;
    @JsonIgnore
    protected final Map<String, Object> result;
    @Schema(
        description = "Состояние",
        name = "status",
        example = "EXECUTION")
    protected final AlgComponentStatus status;

    protected AlgorithmStepInfo(@NonNull AlgorithmInfoParam algorithmInfoParam) {
        this.id = algorithmInfoParam.id();
        this.definitionId = algorithmInfoParam.definitionId();
        this.name = algorithmInfoParam.name();
        this.description = algorithmInfoParam.description();
        this.order = algorithmInfoParam.order();
        this.startTime = algorithmInfoParam.startTime();
        this.endTime = algorithmInfoParam.endTime();
        this.cancelTime = algorithmInfoParam.cancelTime();
        this.result = algorithmInfoParam.result();
        this.status = AlgComponentStatus.getAlgComponentStatus(this);
    }

    public static @NotNull AlgorithmStepInfo createAlgorithmStepInfo(AlgorithmStep algorithmStep,
                                                                     AlgorithmStepDefinition algorithmStepDefinition) {
        return new AlgorithmStepInfo(AlgorithmInfoParam.create(algorithmStepDefinition, algorithmStep));
    }

    @Override
    @JsonIgnore
    public IAlgComponentDefine getDefine() {
        return null;
    }
}
