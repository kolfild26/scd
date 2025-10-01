package ru.sportmaster.scd.service.planlimit;

import java.util.List;
import ru.sportmaster.scd.entity.dictionary.Collection;

public interface PlanLimitSettingsService {
    List<Collection> getAvailableCollections(Long divisionId);
}
