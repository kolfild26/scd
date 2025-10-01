package ru.sportmaster.scd.repository.mementity;

import ru.sportmaster.scd.entity.mementity.DevWareME;
import ru.sportmaster.scd.mementity.MemEntityRepository;
import ru.sportmaster.scd.repository.AbstractJdbcRepository;

public interface DevWareMERepository
    extends MemEntityRepository<Long, DevWareME>, AbstractJdbcRepository {
}
