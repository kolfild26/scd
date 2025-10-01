package ru.sportmaster.scd.repository.mementity;

import static ru.sportmaster.scd.consts.ParamNames.DEV_WARE_ENTITY_NAME;

import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.sportmaster.scd.entity.mementity.DevWareME;
import ru.sportmaster.scd.mementity.MemEntityRepositoryImpl;

@Slf4j
@Repository
@ConditionalOnExpression("${scd.memory.entity.dev-use:true}")
public class DevWareMERepositoryImpl extends MemEntityRepositoryImpl<Long, DevWareME>
    implements DevWareMERepository {
    private static final Integer FETCH_SIZE = 10000;

    public DevWareMERepositoryImpl() {
        super(DEV_WARE_ENTITY_NAME, FETCH_SIZE, DevWareME.class);
    }

    @Override
    protected DevWareME createFromResultSet(@NonNull ResultSet resultSet) throws SQLException {
        return DevWareME.resultSetMapping(resultSet);
    }

    @Override
    protected  Long getKey(DevWareME obj) {
        return obj.getId();
    }
}
