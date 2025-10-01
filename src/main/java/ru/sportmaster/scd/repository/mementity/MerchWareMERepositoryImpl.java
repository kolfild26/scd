package ru.sportmaster.scd.repository.mementity;

import static ru.sportmaster.scd.consts.ParamNames.MERCH_WARE_ENTITY_NAME;

import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.sportmaster.scd.entity.mementity.MerchWareME;
import ru.sportmaster.scd.mementity.MemEntityRepositoryImpl;

@Slf4j
@Repository
@ConditionalOnExpression("${scd.memory.entity.merch-use:true}")
public class MerchWareMERepositoryImpl extends MemEntityRepositoryImpl<Long, MerchWareME>
    implements MerchWareMERepository {
    private static final Integer FETCH_SIZE = 50000;

    public MerchWareMERepositoryImpl() {
        super(MERCH_WARE_ENTITY_NAME, FETCH_SIZE, MerchWareME.class);
    }

    @Override
    protected MerchWareME createFromResultSet(@NonNull ResultSet resultSet) throws SQLException {
        return MerchWareME.resultSetMapping(resultSet);
    }

    @Override
    protected  Long getKey(MerchWareME obj) {
        return obj.getId();
    }
}
