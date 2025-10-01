package ru.sportmaster.scd.service.pivot;

import jakarta.persistence.StoredProcedureQuery;
import javax.sql.DataSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import ru.sportmaster.scd.entity.struct.TpPivotConfig;
import ru.sportmaster.scd.entity.struct.TpPivotConfigParams;
import ru.sportmaster.scd.entity.struct.TpPivotFilter;
import ru.sportmaster.scd.struct.IStructMapper;

public interface PivotCallService {
    DataSource getDataSource();

    SimpleJdbcCall getPivotConfigCall();

    SimpleJdbcCall getSavePivotConfigCall(DataSource dataSource,
                                          IStructMapper<TpPivotConfigParams> tpPivotConfigParamsMapper,
                                          IStructMapper<TpPivotConfig> tpPivotConfigMapper);

    SimpleJdbcCall getFilterSetGetterCall();

    SimpleJdbcCall getSaveFilterSetCall(DataSource dataSource,
                                        IStructMapper<TpPivotFilter> tpPivotFilterStructMapper);

    SimpleJdbcCall getDeleteFilterSetsCall(DataSource dataSource);

    StoredProcedureQuery getPivotDataQuery();

    StoredProcedureQuery getSearchPivotRowsQuery();

    StoredProcedureQuery getDownloadPivotDataQuery();

    StoredProcedureQuery getProgressPivotQuery();

    StoredProcedureQuery getPivotUniqFieldValuesQuery();
}
