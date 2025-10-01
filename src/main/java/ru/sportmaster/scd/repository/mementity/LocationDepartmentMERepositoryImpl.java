package ru.sportmaster.scd.repository.mementity;

import static ru.sportmaster.scd.consts.ParamNames.LOCATION_DEPARTMENT_ENTITY_NAME;

import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.sportmaster.scd.entity.mementity.LocationDepartmentME;
import ru.sportmaster.scd.mementity.MemEntityRepositoryImpl;

@Slf4j
@Getter
@Repository
public class LocationDepartmentMERepositoryImpl extends MemEntityRepositoryImpl<Long, LocationDepartmentME>
    implements LocationDepartmentMERepository {
    private static final Integer FETCH_SIZE = 10000;

    public LocationDepartmentMERepositoryImpl() {
        super(LOCATION_DEPARTMENT_ENTITY_NAME, FETCH_SIZE, LocationDepartmentME.class);
    }

    @Override
    protected LocationDepartmentME createFromResultSet(@NonNull ResultSet resultSet) throws SQLException {
        return LocationDepartmentME.resultSetMapping(resultSet);
    }

    @Override
    protected Long getKey(LocationDepartmentME obj) {
        return obj.getId();
    }
}
