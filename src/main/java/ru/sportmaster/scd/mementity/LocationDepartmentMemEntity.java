package ru.sportmaster.scd.mementity;

import static ru.sportmaster.scd.consts.ParamNames.LOCATION_DEPARTMENT_ENTITY_NAME;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.mementity.LocationDepartmentME;
import ru.sportmaster.scd.repository.mementity.LocationDepartmentMERepository;

@Component
public class LocationDepartmentMemEntity extends AbstractMemEntity<Long, LocationDepartmentME> {
    private static final String MAP_NAME = "locationDepartmentMapEntity";

    public LocationDepartmentMemEntity(@Value("${scd.memory.entity.minute-reload:360}") Long reloadPeriod,
                                       LocationDepartmentMERepository repository) {
        super(MAP_NAME, repository, LOCATION_DEPARTMENT_ENTITY_NAME, reloadPeriod);
    }
}
