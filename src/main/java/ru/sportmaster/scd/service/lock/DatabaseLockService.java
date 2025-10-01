package ru.sportmaster.scd.service.lock;

import org.springframework.lang.NonNull;
import ru.sportmaster.scd.entity.algorithms.definitions.AlgPartitionType;

public interface DatabaseLockService {
    boolean tryLock(Long idPartition,
                    @NonNull AlgPartitionType partitionType);

    void tryUnLock(Long idPartition,
                   @NonNull AlgPartitionType partitionType);
}
