package ru.sportmaster.scd.service.algorithms;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.sportmaster.scd.entity.algorithms.definitions.AlgPartitionType;
import ru.sportmaster.scd.service.lock.DatabaseLockService;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlgorithmLockServiceImpl implements AlgorithmLockService {
    private final DatabaseLockService databaseLockService;

    @Override
    public boolean lock(Long idPartition,
                        @NonNull AlgPartitionType partitionType) {
        try {
            return
                databaseLockService.tryLock(
                    idPartition,
                    partitionType
                );
        } catch (Exception e) {
            log.error("Lock BD error: {}", e.getMessage(), e);
        }
        return false;
    }

    @Override
    public void unLock(Long idPartition,
                       @NonNull AlgPartitionType partitionType) {
        try {
            databaseLockService.tryUnLock(
                idPartition,
                partitionType
            );
        } catch (Exception e) {
            log.error("UnLock BD error: {}", e.getMessage(), e);
        }
    }
}
