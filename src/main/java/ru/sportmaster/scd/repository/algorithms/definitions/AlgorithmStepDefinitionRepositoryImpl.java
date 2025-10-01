package ru.sportmaster.scd.repository.algorithms.definitions;

import org.springframework.stereotype.Repository;
import ru.sportmaster.scd.entity.algorithms.definitions.AlgorithmStepDefinition;
import ru.sportmaster.scd.repository.AbstractRepositoryImpl;

@Repository
public class AlgorithmStepDefinitionRepositoryImpl
    extends AbstractRepositoryImpl<AlgorithmStepDefinition, Long>
    implements AlgorithmStepDefinitionRepository {
    public AlgorithmStepDefinitionRepositoryImpl() {
        super(AlgorithmStepDefinition.class);
    }
}
