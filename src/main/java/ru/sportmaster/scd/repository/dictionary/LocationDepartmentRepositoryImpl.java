package ru.sportmaster.scd.repository.dictionary;

import org.springframework.stereotype.Repository;
import ru.sportmaster.scd.entity.pivot.LocationDepartment;
import ru.sportmaster.scd.repository.AbstractRepositoryImpl;

@Repository
public class LocationDepartmentRepositoryImpl extends AbstractRepositoryImpl<LocationDepartment, Long>
    implements LocationDepartmentRepository {
    public LocationDepartmentRepositoryImpl() {
        super(LocationDepartment.class);
    }
}
