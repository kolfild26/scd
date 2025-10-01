package ru.sportmaster.scd.dto;

import static ru.sportmaster.scd.utils.ConvertUtil.localDateTimeToString;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sportmaster.scd.entity.algorithms.definitions.AlgPartitionType;
import ru.sportmaster.scd.task.AlgorithmTask;
import ru.sportmaster.scd.task.AlgorithmTaskState;
import ru.sportmaster.scd.task.AlgorithmTaskStatus;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AlgorithmQueueDto {
    @Schema(
        description = "Идентификатор алгоритма",
        name = "algorithmId",
        requiredMode = Schema.RequiredMode.REQUIRED,
        example = "1")
    private Long algorithmId;
    @Schema(
        description = "Тип секционирования",
        name = "partitionType",
        requiredMode = Schema.RequiredMode.REQUIRED,
        example = "BY_ID_PARTITION_DIV_TMA")
    private AlgPartitionType partitionType;
    @Schema(
        description = "Идентификатор секционирования",
        name = "partitionId",
        requiredMode = Schema.RequiredMode.REQUIRED,
        example = "1")
    private Long partitionId;
    @Schema(
        description = "Состояние",
        name = "status",
        example = "QUEUED")
    private AlgorithmTaskStatus status;
    @Schema(
        description = "Время создания",
        name = "created",
        implementation = String.class,
        example = "2022-10-29T12:00:00")
    private String created;
    @Schema(
        description = "Время запуска",
        name = "started",
        implementation = String.class,
        example = "2022-10-29T12:00:00")
    private String started;
    @Schema(
        description = "Время отмены",
        name = "canceled",
        implementation = String.class,
        example = "2022-10-29T12:00:00")
    private String canceled;
    @Schema(
        description = "Идентификатор группы алгоритмов",
        name = "groupAlgorithm",
        requiredMode = Schema.RequiredMode.REQUIRED,
        example = "9223372036854775807")
    private String groupAlgorithm;
    @Schema(
        description = "Идентификатор расчета",
        name = "calcId",
        example = "1")
    private Long calcId;
    @Schema(
        description = "Остановить расчет, в случае ошибки",
        name = "isStopOnError",
        example = "true")
    private boolean isStopOnError;

    public static AlgorithmQueueDto create(AlgorithmTask task,
                                           AlgorithmTaskState state) {
        return AlgorithmQueueDto.builder()
            .algorithmId(task.getAlgorithmId())
            .partitionType(task.getPartitionType())
            .partitionId(task.getPartitionId())
            .status(state.getStatus())
            .created(localDateTimeToString(state.getCreated()))
            .started(localDateTimeToString(state.getStarted()))
            .canceled(localDateTimeToString(state.getCanceled()))
            .groupAlgorithm(Optional.ofNullable(task.getGroupAlgorithm()).map(String::valueOf).orElse(null))
            .calcId(task.getCalcId())
            .isStopOnError(task.isStopOnError())
            .build();
    }
}
