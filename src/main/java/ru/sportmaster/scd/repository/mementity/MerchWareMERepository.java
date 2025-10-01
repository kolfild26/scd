package ru.sportmaster.scd.repository.mementity;

import ru.sportmaster.scd.entity.mementity.MerchWareME;
import ru.sportmaster.scd.mementity.MemEntityRepository;
import ru.sportmaster.scd.repository.AbstractJdbcRepository;

public interface MerchWareMERepository
    extends MemEntityRepository<Long, MerchWareME>, AbstractJdbcRepository {
}
