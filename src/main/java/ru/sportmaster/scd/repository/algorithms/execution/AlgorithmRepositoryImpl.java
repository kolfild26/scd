package ru.sportmaster.scd.repository.algorithms.execution;

import static ru.sportmaster.scd.consts.ParamNames.FRONT_API_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.GET_ALGORITHM_DURATION_PROCEDURE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.HOST_FIELD;
import static ru.sportmaster.scd.consts.ParamNames.IP_ADDRESS_FIELD;
import static ru.sportmaster.scd.consts.ParamNames.MODULE_FIELD;
import static ru.sportmaster.scd.consts.ParamNames.OS_USER_FIELD;
import static ru.sportmaster.scd.consts.ParamNames.P_ALG_DEF_ID;
import static ru.sportmaster.scd.consts.ParamNames.P_DURATION_MS;
import static ru.sportmaster.scd.consts.ParamNames.P_LOG_GROUP;
import static ru.sportmaster.scd.consts.ParamNames.SCD_API_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamNames.USER_ID_FIELD;
import static ru.sportmaster.scd.utils.AlgorithmUtils.addParamsValues;
import static ru.sportmaster.scd.utils.HistoryUtils.getCurrentRevision;
import static ru.sportmaster.scd.web.UserEnvArgumentResolver.getOrGenerateRequestUuid;

import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.sportmaster.scd.algorithms.ComponentType;
import ru.sportmaster.scd.entity.algorithms.definitions.AlgorithmDefinition;
import ru.sportmaster.scd.entity.algorithms.definitions.AlgorithmDefinition_;
import ru.sportmaster.scd.entity.algorithms.execution.Algorithm;
import ru.sportmaster.scd.entity.algorithms.execution.Algorithm_;
import ru.sportmaster.scd.exceptions.AlgComponentNotFoundException;
import ru.sportmaster.scd.repository.AbstractRepositoryImpl;

@Repository
public class AlgorithmRepositoryImpl
    extends AbstractRepositoryImpl<Algorithm, Long>
    implements AlgorithmRepository {

    public AlgorithmRepositoryImpl() {
        super(Algorithm.class);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Algorithm create(@NonNull AlgorithmDefinition algorithmDefinition,
                            @NonNull Map<String, Object> params) {
        addParamsValues(params, algorithmDefinition.getDefaultParams());
        return persist(
            Algorithm.builder()
                .algorithmDefinition(algorithmDefinition)
                .defVersion(
                    getCurrentRevision(
                        AuditReaderFactory.get(em),
                        AlgorithmDefinition.class,
                        algorithmDefinition.getId()
                    )
                )
                .params(new HashMap<>(params))
                .createTime(LocalDateTime.now())
                .user((Long) params.get(USER_ID_FIELD))
                .osUser((String) params.get(OS_USER_FIELD))
                .host((String) params.get(HOST_FIELD))
                .ipAddress((String) params.get(IP_ADDRESS_FIELD))
                .module((String) params.get(MODULE_FIELD))
                .build()
        );
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Algorithm start(@NonNull Long algorithmId) {
        var algorithm =
            Optional.ofNullable(findById(algorithmId))
                .orElseThrow(() -> new AlgComponentNotFoundException(algorithmId, ComponentType.ALGORITHM));
        algorithm.setStartTime(LocalDateTime.now());
        return algorithm;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void finish(@NonNull Long algorithmId,
                       @NonNull Map<String, Object> params) {
        var algorithm =
            Optional.ofNullable(findById(algorithmId))
                .orElseThrow(() -> new AlgComponentNotFoundException(algorithmId, ComponentType.ALGORITHM));
        algorithm.setResult(new HashMap<>(params));
        algorithm.setEndTime(LocalDateTime.now());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void cancel(@NonNull Long algorithmId,
                       @NonNull LocalDateTime cancelTime) {
        var algorithm =
            Optional.ofNullable(findById(algorithmId))
                .orElseThrow(() -> new AlgComponentNotFoundException(algorithmId, ComponentType.ALGORITHM));
        algorithm.setCancelTime(cancelTime);
    }

    @Override
    public Algorithm getLastAlgorithm(Long algorithmDefId, boolean isFinished) {
        var criteriaBuilder = em.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Algorithm.class);
        var root = query.from(Algorithm.class);

        var restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.and(
            criteriaBuilder.equal(
                root.get(Algorithm_.ALGORITHM_DEFINITION).get(AlgorithmDefinition_.ID),
                algorithmDefId
            ),
            criteriaBuilder.isNull(root.get(Algorithm_.CANCEL_TIME)),
            isFinished
                ? criteriaBuilder.isNotNull(root.get(Algorithm_.END_TIME))
                : criteriaBuilder.isNull(root.get(Algorithm_.END_TIME))
        ));
        query.orderBy(criteriaBuilder.desc(root.get(Algorithm_.ID)));

        return em.createQuery(query.select(root).where(restrictions))
            .setMaxResults(1)
            .getResultStream()
            .findFirst()
            .orElse(null);
    }

    @Override
    public Long getAlgDuration(Long algorithmDefId) {
        StoredProcedureQuery query = em.createStoredProcedureQuery(
            SCD_API_SCHEMA_NAME + '.' + FRONT_API_PACKAGE_NAME + '.' + GET_ALGORITHM_DURATION_PROCEDURE_NAME)
            .registerStoredProcedureParameter(P_ALG_DEF_ID, Long.class, ParameterMode.IN)
            .registerStoredProcedureParameter(P_DURATION_MS, Long.class, ParameterMode.OUT)
            .registerStoredProcedureParameter(P_LOG_GROUP, String.class, ParameterMode.IN);
        query.setParameter(P_ALG_DEF_ID, algorithmDefId);
        query.setParameter(P_LOG_GROUP, getOrGenerateRequestUuid());
        query.execute();
        return (Long) query.getOutputParameterValue(P_DURATION_MS);
    }
}
