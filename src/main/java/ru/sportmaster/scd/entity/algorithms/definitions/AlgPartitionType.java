package ru.sportmaster.scd.entity.algorithms.definitions;

import static ru.sportmaster.scd.consts.ParamNames.P_ID_BUSINESS_TMA;
import static ru.sportmaster.scd.consts.ParamNames.P_ID_PARTITION_DIV_TMA;
import static ru.sportmaster.scd.utils.ConvertUtil.getLongValue;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.lang.NonNull;
import ru.sportmaster.scd.task.IPartitionService;

public enum AlgPartitionType {
    BY_ID_PARTITION_DIV_TMA {
        @Override
        public Long getPartitionId(@NonNull Map<String, Object> params) {
            return getLongValue(params.get(P_ID_PARTITION_DIV_TMA));
        }

        @Override
        public Long getPartitionId(Long idPartition) {
            return
                Optional.ofNullable(idPartition)
                    .orElse(NULL_PARTITION_ID);
        }

        @Override
        public List<Long> getPartitionDivTmaIds(Long idPartition,
                                                @NonNull IPartitionService partitionService) {
            return List.of(idPartition);
        }
    },
    BY_ID_BUSINESS_TMA {
        @Override
        public Long getPartitionId(@NonNull Map<String, Object> params) {
            return getLongValue(params.get(P_ID_BUSINESS_TMA));
        }

        @Override
        public List<Long> getPartitionDivTmaIds(Long idPartition,
                                                @NonNull IPartitionService partitionService) {
            return partitionService.getBusinessPartitionDivTmaIds(idPartition);
        }
    },
    ALL_PARTITIONS {
        @Override
        public Long getPartitionId(@NonNull Map<String, Object> params) {
            return null;
        }

        @Override
        public List<Long> getPartitionDivTmaIds(Long idPartition,
                                                @NonNull IPartitionService partitionService) {
            return partitionService.getAllPartitionDivTmaIds(idPartition);
        }
    };

    private static final Long NULL_PARTITION_ID = 0L;

    public abstract Long getPartitionId(@NonNull Map<String, Object> params);

    public Long getPartitionId(Long idPartition) {
        return idPartition;
    }

    public abstract List<Long> getPartitionDivTmaIds(Long idPartition,
                                                     @NonNull IPartitionService partitionService);
}
