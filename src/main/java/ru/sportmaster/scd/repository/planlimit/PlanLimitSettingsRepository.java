package ru.sportmaster.scd.repository.planlimit;

import java.util.List;
import ru.sportmaster.scd.entity.dictionary.Collection;
import ru.sportmaster.scd.entity.planlimit.PlPriorityMatrix;
import ru.sportmaster.scd.repository.AbstractRepository;

public interface PlanLimitSettingsRepository extends AbstractRepository<PlPriorityMatrix, Long> {
    List<Collection> getExistingCollections(Long divisionId);
}
