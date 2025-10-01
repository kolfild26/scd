package ru.sportmaster.scd.repository.algorithms.execution;

import static ru.sportmaster.scd.utils.AlgorithmUtils.addParamsValues;
import static ru.sportmaster.scd.utils.HistoryUtils.getCurrentRevision;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.sportmaster.scd.entity.algorithms.definitions.AlgorithmStageDefinition;
import ru.sportmaster.scd.entity.algorithms.execution.Algorithm;
import ru.sportmaster.scd.entity.algorithms.execution.AlgorithmStage;
import ru.sportmaster.scd.repository.AbstractRepositoryImpl;

@Repository
public class AlgorithmStageRepositoryImpl
    extends AbstractRepositoryImpl<AlgorithmStage, Long>
    implements AlgorithmStageRepository {
    public AlgorithmStageRepositoryImpl() {
        super(AlgorithmStage.class);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public AlgorithmStage create(@NonNull Algorithm algorithm,
                                 @NonNull AlgorithmStageDefinition algorithmStageDefinition,
                                 @NonNull Map<String, Object> params) {
        addParamsValues(params, algorithmStageDefinition.getDefaultParams());
        return persist(
            AlgorithmStage.builder()
                .algorithm(algorithm)
                .algorithmStageDefinition(algorithmStageDefinition)
                .defStageVersion(
                    getCurrentRevision(
                        AuditReaderFactory.get(em),
                        AlgorithmStageDefinition.class,
                        algorithmStageDefinition.getId()
                    )
                )
                .params(params)
                .startTime(LocalDateTime.now())
                .build()
        );
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void finish(@NonNull Long algorithmStageId,
                       @NonNull Map<String, Object> params) {
        var stage = findById(algorithmStageId);
        stage.setResult(new HashMap<>(params));
        stage.setEndTime(LocalDateTime.now());
    }
}
