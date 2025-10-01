package ru.sportmaster.scd.repository.mementity;

import ru.sportmaster.scd.entity.mementity.LocationDepartmentME;
import ru.sportmaster.scd.mementity.MemEntityRepository;
import ru.sportmaster.scd.repository.AbstractJdbcRepository;

public interface LocationDepartmentMERepository
    extends MemEntityRepository<Long, LocationDepartmentME>, AbstractJdbcRepository {
}
