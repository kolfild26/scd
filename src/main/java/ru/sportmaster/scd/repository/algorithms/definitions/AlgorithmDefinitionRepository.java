package ru.sportmaster.scd.repository.algorithms.definitions;

import java.util.List;
import ru.sportmaster.scd.entity.algorithms.definitions.AlgorithmDefinition;
import ru.sportmaster.scd.repository.AbstractRepository;

public interface AlgorithmDefinitionRepository extends AbstractRepository<AlgorithmDefinition, Long> {
    List<AlgorithmDefinition> findAllNotDeleted();
}
