package ru.sportmaster.scd.task;

import static java.util.Objects.isNull;
import static ru.sportmaster.scd.consts.ParamNames.ALGORITHM_GROUP;
import static ru.sportmaster.scd.consts.ParamNames.ALGORITHM_ID;
import static ru.sportmaster.scd.consts.ParamNames.CALC_ID;
import static ru.sportmaster.scd.consts.ParamNames.PARTITION_TYPE;
import static ru.sportmaster.scd.consts.ParamNames.STOP_ON_ERROR;
import static ru.sportmaster.scd.utils.ConvertUtil.getBooleanValue;
import static ru.sportmaster.scd.utils.ConvertUtil.getIntValue;
import static ru.sportmaster.scd.utils.ConvertUtil.getLongValue;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.sportmaster.scd.entity.algorithms.definitions.AlgPartitionType;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class AlgorithmTask implements Serializable {
    @Serial
    private static final long serialVersionUID = 4567128891707839640L;

    private Long algorithmId;
    private AlgPartitionType partitionType;
    private Long partitionId;
    private Long groupAlgorithm;
    private Long calcId;
    private boolean isStopOnError;

    public static AlgorithmTask createFromParams(Map<String, Object> params) {
        if (isNull(params)) {
            return null;
        }
        var partitionType =
            Optional.ofNullable(getIntValue(params.get(PARTITION_TYPE)))
                .map(partType -> AlgPartitionType.values()[partType])
                .orElse(AlgPartitionType.BY_ID_PARTITION_DIV_TMA);
        return
            AlgorithmTask.builder()
                .algorithmId(getLongValue(params.get(ALGORITHM_ID)))
                .partitionType(partitionType)
                .partitionId(partitionType.getPartitionId(params))
                .groupAlgorithm(getLongValue(params.get(ALGORITHM_GROUP)))
                .calcId(getLongValue(params.get(CALC_ID)))
                .isStopOnError(getBooleanValue(params.get(STOP_ON_ERROR)))
                .build();
    }
}
