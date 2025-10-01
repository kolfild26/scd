package ru.sportmaster.scd.service.algorithms;

import org.springframework.lang.NonNull;
import ru.sportmaster.scd.entity.algorithms.definitions.AlgPartitionType;

public interface AlgorithmLockService {
    boolean lock(Long idPartition,
                 @NonNull AlgPartitionType partitionType);

    void unLock(Long idPartition,
                @NonNull AlgPartitionType partitionType);
}
