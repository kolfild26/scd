package ru.sportmaster.scd.service.lock;

import static ru.sportmaster.scd.consts.ParamNames.LOCK_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.P_ID_PARTITION;
import static ru.sportmaster.scd.consts.ParamNames.P_PARTITION_TYPE;
import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamNames.TRY_LOCK_PROCEDURE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.TRY_UNLOCK_PROCEDURE_NAME;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.sportmaster.scd.entity.algorithms.definitions.AlgPartitionType;

@Slf4j
@Service
@RequiredArgsConstructor
public class OracleLockServiceImpl implements DatabaseLockService {
    @PersistenceContext
    private final EntityManager em;

    @Override
    public boolean tryLock(Long idPartition,
                           @NonNull AlgPartitionType partitionType) {
        return execute(TRY_LOCK_PROCEDURE_NAME, idPartition, partitionType);
    }

    @Override
    public void tryUnLock(Long idPartition,
                          @NonNull AlgPartitionType partitionType) {
        execute(TRY_UNLOCK_PROCEDURE_NAME, idPartition, partitionType);
    }

    private boolean execute(@NonNull String procedureName,
                            Long idPartition,
                            @NonNull AlgPartitionType partitionType) {
        try {
            var query =
                em.createStoredProcedureQuery(getFunctionName(procedureName))
                    .registerStoredProcedureParameter(P_ID_PARTITION, Long.class, ParameterMode.IN)
                    .registerStoredProcedureParameter(P_PARTITION_TYPE, Long.class, ParameterMode.IN);
            query.setParameter(
                P_ID_PARTITION,
                partitionType.getPartitionId(idPartition)
            );
            query.setParameter(
                P_PARTITION_TYPE,
                partitionType.ordinal()
            );
            query.execute();
            return true;
        } catch (Exception exception) {
            log.error(
                "Ошибка выполнения {} (partitionType:{}; idPartition:{}): {}",
                procedureName, partitionType, idPartition, exception.getMessage(),
                exception
            );
            return false;
        }
    }

    private String getFunctionName(String functionName) {
        return SCD_SCHEMA_NAME + '.' + LOCK_PACKAGE_NAME + '.' + functionName;
    }
}
