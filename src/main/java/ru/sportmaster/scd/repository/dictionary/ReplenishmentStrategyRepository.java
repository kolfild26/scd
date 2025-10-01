package ru.sportmaster.scd.repository.dictionary;

import java.util.List;
import ru.sportmaster.scd.entity.replenishment.ReplenishmentStrategy;
import ru.sportmaster.scd.repository.AbstractRepository;

public interface ReplenishmentStrategyRepository extends AbstractRepository<ReplenishmentStrategy, Long> {
    List<ReplenishmentStrategy> findNotDeletedStrategy();
}
