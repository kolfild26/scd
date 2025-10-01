package ru.sportmaster.scd.repository.mementity;

import org.springframework.stereotype.Repository;
import ru.sportmaster.scd.entity.mementity.LocationDepartmentME;
import ru.sportmaster.scd.mementity.MemEntityCheckProcessorRepositoryImpl;

@Repository
public class LocationDepartmentMECheckProcessorRepositoryImpl
    extends MemEntityCheckProcessorRepositoryImpl<Long>
    implements LocationDepartmentMECheckProcessorRepository {

    public LocationDepartmentMECheckProcessorRepositoryImpl() {
        super(LocationDepartmentME.class);
    }

    @Override
    protected Long getLongKey(Long key) {
        return key;
    }
}
