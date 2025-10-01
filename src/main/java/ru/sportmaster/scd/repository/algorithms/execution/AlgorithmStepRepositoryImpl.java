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
import ru.sportmaster.scd.entity.algorithms.definitions.AlgorithmStepDefinition;
import ru.sportmaster.scd.entity.algorithms.execution.AlgorithmStage;
import ru.sportmaster.scd.entity.algorithms.execution.AlgorithmStep;
import ru.sportmaster.scd.repository.AbstractRepositoryImpl;

@Repository
public class AlgorithmStepRepositoryImpl
    extends AbstractRepositoryImpl<AlgorithmStep, Long>
    implements AlgorithmStepRepository {
    public AlgorithmStepRepositoryImpl() {
        super(AlgorithmStep.class);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public AlgorithmStep create(@NonNull AlgorithmStage algorithmStage,
                                @NonNull AlgorithmStepDefinition algorithmStepDefinition,
                                @NonNull Map<String, Object> params) {
        addParamsValues(params, algorithmStepDefinition.getDefaultParams());
        return persist(
            AlgorithmStep.builder()
                .algorithmStage(algorithmStage)
                .algorithmStepDefinition(algorithmStepDefinition)
                .defStepVersion(
                    getCurrentRevision(
                        AuditReaderFactory.get(em),
                        AlgorithmStepDefinition.class,
                        algorithmStepDefinition.getId()
                    )
                )
                .params(params)
                .startTime(LocalDateTime.now())
                .build()
        );
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void finish(@NonNull Long algorithmStepId,
                       @NonNull Map<String, Object> params) {
        var step = findById(algorithmStepId);
        step.setResult(new HashMap<>(params));
        step.setEndTime(LocalDateTime.now());
    }
}
