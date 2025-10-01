package ru.sportmaster.scd.repository.algorithms.definitions;

import org.springframework.stereotype.Repository;
import ru.sportmaster.scd.entity.algorithms.definitions.AlgorithmStageDefinition;
import ru.sportmaster.scd.repository.AbstractRepositoryImpl;

@Repository
public class AlgorithmStageDefinitionRepositoryImpl
    extends AbstractRepositoryImpl<AlgorithmStageDefinition, Long>
    implements AlgorithmStageDefinitionRepository {
    public AlgorithmStageDefinitionRepositoryImpl() {
        super(AlgorithmStageDefinition.class);
    }
}
