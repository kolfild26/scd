package ru.sportmaster.scd.service.pivot;

import static java.util.Objects.isNull;
import static ru.sportmaster.scd.consts.ParamNames.DELETE_FILTER_SETS_PROCEDURE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.DOWNLOAD_PIVOT_DATA_PROCEDURE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.FRONT_API_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.GET_PIVOT_CONFIG_PROCEDURE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.GET_PIVOT_DATA_PROCEDURE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.GET_PIVOT_FILTERS_PROCEDURE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.GET_PIVOT_UNIQ_FIELD_VALUES_PROCEDURE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.GET_SAVE_PROGRESS_PROCEDURE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.P_COLLAPSED_ORDER_ROWS;
import static ru.sportmaster.scd.consts.ParamNames.P_END_ROW;
import static ru.sportmaster.scd.consts.ParamNames.P_FIELD;
import static ru.sportmaster.scd.consts.ParamNames.P_LOG_GROUP;
import static ru.sportmaster.scd.consts.ParamNames.P_NAMES;
import static ru.sportmaster.scd.consts.ParamNames.P_PIVOT_CONFIG;
import static ru.sportmaster.scd.consts.ParamNames.P_PIVOT_CONFIG_PARAMS;
import static ru.sportmaster.scd.consts.ParamNames.P_PIVOT_DATA;
import static ru.sportmaster.scd.consts.ParamNames.P_PIVOT_FILTER;
import static ru.sportmaster.scd.consts.ParamNames.P_PIVOT_FILTERS;
import static ru.sportmaster.scd.consts.ParamNames.P_PIVOT_NAME;
import static ru.sportmaster.scd.consts.ParamNames.P_PIVOT_ROWS;
import static ru.sportmaster.scd.consts.ParamNames.P_PROGRESS;
import static ru.sportmaster.scd.consts.ParamNames.P_START_ROW;
import static ru.sportmaster.scd.consts.ParamNames.P_TEXT;
import static ru.sportmaster.scd.consts.ParamNames.P_USER_ID;
import static ru.sportmaster.scd.consts.ParamNames.P_VALUES;
import static ru.sportmaster.scd.consts.ParamNames.SAVE_FILTER_SET_PROCEDURE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.SAVE_PIVOT_CONFIG_PROCEDURE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.SCD_API_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamNames.SEARCH_PIVOT_ROWS_PROCEDURE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.STRING_ARRAY_TYPE_NAME;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import java.math.BigDecimal;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import oracle.jdbc.OracleTypes;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.stereotype.Service;
import ru.sportmaster.scd.entity.struct.TpPivotConfig;
import ru.sportmaster.scd.entity.struct.TpPivotConfigParams;
import ru.sportmaster.scd.entity.struct.TpPivotFilter;
import ru.sportmaster.scd.exceptions.DataSourceNotFoundException;
import ru.sportmaster.scd.struct.IStructMapper;
import ru.sportmaster.scd.struct.IStructMapperFactory;
import ru.sportmaster.scd.struct.types.SqlReturnStruct;
import ru.sportmaster.scd.struct.types.SqlReturnStructArray;

@Service
@RequiredArgsConstructor
public class PivotCallServiceImpl implements PivotCallService {
    private final IStructMapperFactory structMapperFactory;

    @PersistenceContext
    private EntityManager em;

    @Override
    public DataSource getDataSource() {
        var dataSource = ((EntityManagerFactoryInfo) em.getEntityManagerFactory()).getDataSource();
        if (dataSource instanceof HikariDataSource hikariDataSource) {
            dataSource = hikariDataSource.getDataSource();
        }
        if (isNull(dataSource)) {
            throw new DataSourceNotFoundException();
        }
        return dataSource;
    }

    @Override
    public SimpleJdbcCall getPivotConfigCall() {
        var dataSource = getDataSource();
        var tpPivotConfigMapper = structMapperFactory.getStructMapper(TpPivotConfig.class);
        var jdbcTemplate = new JdbcTemplate(dataSource);
        return new SimpleJdbcCall(jdbcTemplate)
            .withSchemaName(SCD_API_SCHEMA_NAME)
            .withCatalogName(FRONT_API_PACKAGE_NAME)
            .withProcedureName(GET_PIVOT_CONFIG_PROCEDURE_NAME)
            .declareParameters(
                new SqlParameter(P_PIVOT_NAME, OracleTypes.VARCHAR),
                new SqlParameter(P_USER_ID, OracleTypes.NUMBER),
                new SqlOutParameter(
                    P_PIVOT_CONFIG,
                    OracleTypes.STRUCT,
                    tpPivotConfigMapper.getFullTypeName(),
                    new SqlReturnStruct(tpPivotConfigMapper)
                ),
                new SqlParameter(P_LOG_GROUP, OracleTypes.VARCHAR)
            );
    }

    @Override
    public SimpleJdbcCall getSavePivotConfigCall(DataSource dataSource,
                                                 IStructMapper<TpPivotConfigParams> tpPivotConfigParamsMapper,
                                                 IStructMapper<TpPivotConfig> tpPivotConfigMapper) {
        var jdbcTemplate = new JdbcTemplate(dataSource);
        return new SimpleJdbcCall(jdbcTemplate)
            .withSchemaName(SCD_API_SCHEMA_NAME)
            .withCatalogName(FRONT_API_PACKAGE_NAME)
            .withProcedureName(SAVE_PIVOT_CONFIG_PROCEDURE_NAME)
            .declareParameters(
                new SqlParameter(P_PIVOT_NAME, OracleTypes.VARCHAR),
                new SqlParameter(P_USER_ID, OracleTypes.NUMBER),
                new SqlParameter(
                    P_PIVOT_CONFIG_PARAMS,
                    OracleTypes.STRUCT,
                    tpPivotConfigParamsMapper.getFullTypeName()
                ),
                new SqlOutParameter(
                    P_PIVOT_CONFIG,
                    OracleTypes.STRUCT,
                    tpPivotConfigMapper.getFullTypeName(),
                    new SqlReturnStruct(tpPivotConfigMapper)
                ),
                new SqlParameter(P_LOG_GROUP, OracleTypes.VARCHAR)
            );
    }

    @Override
    public SimpleJdbcCall getFilterSetGetterCall() {
        var dataSource = getDataSource();
        var tpPivotFilterMapper = structMapperFactory.getStructMapper(TpPivotFilter.class);
        var jdbcTemplate = new JdbcTemplate(dataSource);
        return new SimpleJdbcCall(jdbcTemplate)
            .withSchemaName(SCD_API_SCHEMA_NAME)
            .withCatalogName(FRONT_API_PACKAGE_NAME)
            .withProcedureName(GET_PIVOT_FILTERS_PROCEDURE_NAME)
            .declareParameters(
                new SqlParameter(P_PIVOT_NAME, OracleTypes.VARCHAR),
                new SqlParameter(P_USER_ID, OracleTypes.NUMBER),
                new SqlOutParameter(
                    P_PIVOT_FILTERS,
                    OracleTypes.ARRAY,
                    tpPivotFilterMapper.getFullArrayName(),
                    new SqlReturnStructArray<>(tpPivotFilterMapper)
                ),
                new SqlParameter(P_LOG_GROUP, OracleTypes.VARCHAR)
            );
    }

    @Override
    public SimpleJdbcCall getSaveFilterSetCall(DataSource dataSource,
                                               IStructMapper<TpPivotFilter> tpPivotFilterStructMapper) {
        var jdbcTemplate = new JdbcTemplate(dataSource);
        return new SimpleJdbcCall(jdbcTemplate)
            .withSchemaName(SCD_API_SCHEMA_NAME)
            .withCatalogName(FRONT_API_PACKAGE_NAME)
            .withProcedureName(SAVE_FILTER_SET_PROCEDURE_NAME)
            .declareParameters(
                new SqlParameter(P_PIVOT_NAME, OracleTypes.VARCHAR),
                new SqlParameter(P_USER_ID, OracleTypes.NUMBER),
                new SqlParameter(
                    P_PIVOT_FILTER,
                    OracleTypes.STRUCT,
                    tpPivotFilterStructMapper.getFullTypeName()
                ),
                new SqlParameter(P_LOG_GROUP, OracleTypes.VARCHAR)
            );
    }

    @Override
    public SimpleJdbcCall getDeleteFilterSetsCall(DataSource dataSource) {
        var jdbcTemplate = new JdbcTemplate(dataSource);
        return new SimpleJdbcCall(jdbcTemplate)
            .withSchemaName(SCD_API_SCHEMA_NAME)
            .withCatalogName(FRONT_API_PACKAGE_NAME)
            .withProcedureName(DELETE_FILTER_SETS_PROCEDURE_NAME)
            .declareParameters(
                new SqlParameter(P_PIVOT_NAME, OracleTypes.VARCHAR),
                new SqlParameter(P_USER_ID, OracleTypes.NUMBER),
                new SqlParameter(
                    P_NAMES,
                    OracleTypes.ARRAY,
                    STRING_ARRAY_TYPE_NAME
                ),
                new SqlParameter(P_LOG_GROUP, OracleTypes.VARCHAR)
            );
    }

    @Override
    public StoredProcedureQuery getPivotDataQuery() {
        return em
            .createStoredProcedureQuery(
                SCD_API_SCHEMA_NAME + '.' + FRONT_API_PACKAGE_NAME + '.' + GET_PIVOT_DATA_PROCEDURE_NAME)
            .registerStoredProcedureParameter(
                P_PIVOT_NAME,
                String.class,
                ParameterMode.IN)
            .registerStoredProcedureParameter(
                P_USER_ID,
                Long.class,
                ParameterMode.IN)
            .registerStoredProcedureParameter(
                P_START_ROW,
                Long.class,
                ParameterMode.IN)
            .registerStoredProcedureParameter(
                P_END_ROW,
                Long.class,
                ParameterMode.IN)
            .registerStoredProcedureParameter(
                P_PIVOT_DATA,
                Class.class,
                ParameterMode.REF_CURSOR)
            .registerStoredProcedureParameter(
                P_COLLAPSED_ORDER_ROWS,
                String.class,
                ParameterMode.IN)
            .registerStoredProcedureParameter(
                P_LOG_GROUP,
                String.class,
                ParameterMode.IN);
    }

    @Override
    public StoredProcedureQuery getSearchPivotRowsQuery() {
        return em
            .createStoredProcedureQuery(
                SCD_API_SCHEMA_NAME + '.' + FRONT_API_PACKAGE_NAME + '.' + SEARCH_PIVOT_ROWS_PROCEDURE_NAME)
            .registerStoredProcedureParameter(
                P_PIVOT_NAME,
                String.class,
                ParameterMode.IN)
            .registerStoredProcedureParameter(
                P_USER_ID,
                Long.class,
                ParameterMode.IN)
            .registerStoredProcedureParameter(
                P_TEXT,
                String.class,
                ParameterMode.IN)
            .registerStoredProcedureParameter(
                P_PIVOT_ROWS,
                Class.class,
                ParameterMode.REF_CURSOR)
            .registerStoredProcedureParameter(
                P_LOG_GROUP,
                String.class,
                ParameterMode.IN);
    }

    @Override
    public StoredProcedureQuery getDownloadPivotDataQuery() {
        return em
            .createStoredProcedureQuery(
                SCD_API_SCHEMA_NAME + '.' + FRONT_API_PACKAGE_NAME + '.' + DOWNLOAD_PIVOT_DATA_PROCEDURE_NAME)
            .registerStoredProcedureParameter(
                P_PIVOT_NAME,
                String.class,
                ParameterMode.IN)
            .registerStoredProcedureParameter(
                P_USER_ID,
                Long.class,
                ParameterMode.IN)
            .registerStoredProcedureParameter(
                P_PIVOT_DATA,
                Class.class,
                ParameterMode.REF_CURSOR)
            .registerStoredProcedureParameter(
                P_LOG_GROUP,
                String.class,
                ParameterMode.IN);
    }

    @Override
    public StoredProcedureQuery getProgressPivotQuery() {
        return em
            .createStoredProcedureQuery(
                SCD_API_SCHEMA_NAME + '.' + FRONT_API_PACKAGE_NAME + '.' + GET_SAVE_PROGRESS_PROCEDURE_NAME)
            .registerStoredProcedureParameter(
                P_PIVOT_NAME,
                String.class,
                ParameterMode.IN)
            .registerStoredProcedureParameter(
                P_USER_ID,
                Long.class,
                ParameterMode.IN)
            .registerStoredProcedureParameter(
                P_PROGRESS,
                BigDecimal.class,
                ParameterMode.OUT)
            .registerStoredProcedureParameter(
                P_LOG_GROUP,
                String.class,
                ParameterMode.IN);
    }

    @Override
    public StoredProcedureQuery getPivotUniqFieldValuesQuery() {
        return em
            .createStoredProcedureQuery(
                SCD_API_SCHEMA_NAME + '.' + FRONT_API_PACKAGE_NAME + '.'
                    + GET_PIVOT_UNIQ_FIELD_VALUES_PROCEDURE_NAME)
            .registerStoredProcedureParameter(
                P_PIVOT_NAME,
                String.class,
                ParameterMode.IN)
            .registerStoredProcedureParameter(
                P_USER_ID,
                Long.class,
                ParameterMode.IN)
            .registerStoredProcedureParameter(
                P_FIELD,
                String.class,
                ParameterMode.IN)
            .registerStoredProcedureParameter(
                P_VALUES,
                Class.class,
                ParameterMode.REF_CURSOR)
            .registerStoredProcedureParameter(
                P_LOG_GROUP,
                String.class,
                ParameterMode.IN);
    }
}
