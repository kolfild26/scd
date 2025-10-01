package ru.sportmaster.scd.service.pivot;

import static java.util.Objects.isNull;
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
import static ru.sportmaster.scd.utils.ConvertUtil.clobToString;
import static ru.sportmaster.scd.utils.StructUtil.getOracleConnection;
import static ru.sportmaster.scd.utils.StructUtil.stringListToArray;
import static ru.sportmaster.scd.web.UserEnvArgumentResolver.getOrGenerateRequestUuid;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.transaction.NotSupportedException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sportmaster.scd.dto.pivot.DownloadType;
import ru.sportmaster.scd.dto.pivot.PivotDownloadRequestDto;
import ru.sportmaster.scd.dto.pivot.PivotPreparingFileDto;
import ru.sportmaster.scd.entity.struct.TpPivotConfig;
import ru.sportmaster.scd.entity.struct.TpPivotConfigParams;
import ru.sportmaster.scd.entity.struct.TpPivotFilter;
import ru.sportmaster.scd.exceptions.ExecutingRuntimeException;
import ru.sportmaster.scd.service.export.PivotExportDataService;
import ru.sportmaster.scd.struct.IStructMapper;
import ru.sportmaster.scd.struct.IStructMapperFactory;

@Slf4j
@Service
@RequiredArgsConstructor
public class PivotDAOServiceImpl implements PivotDAOService {
    private final IStructMapperFactory structMapperFactory;
    private final PivotCallService pivotCallService;
    private final List<PivotExportDataService> pivotExportDataServices;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    @Transactional
    public TpPivotConfig getPivotConfig(@NonNull String pivotName,
                                        @NonNull Long userId) {
        return getPivotConfigFromDatabase(pivotName, userId);
    }

    private TpPivotConfig getPivotConfigFromDatabase(@NonNull String pivotName,
                                                     @NonNull Long userId) {
        var call = pivotCallService.getPivotConfigCall();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(P_PIVOT_NAME, pivotName);
        params.addValue(P_USER_ID, userId);
        params.addValue(P_LOG_GROUP, getOrGenerateRequestUuid());
        Map<String, Object> resultMap = call.execute(params);
        return (TpPivotConfig) resultMap.get(P_PIVOT_CONFIG);
    }

    @Override
    @Transactional
    public TpPivotConfig savePivotConfig(@NonNull String pivotName,
                                         @NonNull Long userId,
                                         TpPivotConfigParams pivotConfigParams) {
        var dataSource = pivotCallService.getDataSource();
        var tpPivotConfigParamsMapper = structMapperFactory.getStructMapper(TpPivotConfigParams.class);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(P_PIVOT_NAME, pivotName);
        params.addValue(P_USER_ID, userId);
        var struct = getStructTpPivotConfigParams(pivotConfigParams, tpPivotConfigParamsMapper, dataSource);
        params.addValue(P_PIVOT_CONFIG_PARAMS, struct);
        params.addValue(P_LOG_GROUP, getOrGenerateRequestUuid());
        var tpPivotConfigMapper = structMapperFactory.getStructMapper(TpPivotConfig.class);
        Map<String, Object> resultMap =
            pivotCallService
                .getSavePivotConfigCall(dataSource, tpPivotConfigParamsMapper, tpPivotConfigMapper).execute(params);
        return (TpPivotConfig) resultMap.get(P_PIVOT_CONFIG);
    }

    private Struct getStructTpPivotConfigParams(TpPivotConfigParams pivotConfigParams,
                                                IStructMapper<TpPivotConfigParams> tpPivotConfigParamsMapper,
                                                DataSource dataSource) {
        try {
            var connection = getOracleConnection(dataSource);
            try {
                return tpPivotConfigParamsMapper.toStruct(pivotConfigParams, connection);
            } finally {
                DataSourceUtils.releaseConnection(connection, dataSource);
            }
        } catch (SQLException e) {
            throw new ExecutingRuntimeException(e);
        }
    }

    @Override
    @Transactional
    public List<TpPivotFilter> getFilterSets(@NonNull String pivotName,
                                             @NonNull Long userId) {
        var call = pivotCallService.getFilterSetGetterCall();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(P_PIVOT_NAME, pivotName);
        params.addValue(P_USER_ID, userId);
        params.addValue(P_LOG_GROUP, getOrGenerateRequestUuid());
        Map<String, Object> resultMap = call.execute(params);

        List<TpPivotFilter> filterSets = null;
        Object pivotFilters = resultMap.get(P_PIVOT_FILTERS);
        if (pivotFilters != null) {
            filterSets = Stream.of((Object[]) pivotFilters)
                .map(o -> (TpPivotFilter) o)
                .toList();
        }
        return filterSets;
    }

    @Override
    @Transactional
    public void saveFilterSet(@NonNull String pivotName,
                              @NonNull Long userId,
                              TpPivotFilter pivotFilter) {
        var dataSource = pivotCallService.getDataSource();
        var tpPivotFilterMapper = structMapperFactory.getStructMapper(TpPivotFilter.class);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(P_PIVOT_NAME, pivotName);
        params.addValue(P_USER_ID, userId);
        var struct = getStructTpPivotFilter(pivotFilter, tpPivotFilterMapper, dataSource);
        params.addValue(P_PIVOT_FILTER, struct);
        params.addValue(P_LOG_GROUP, getOrGenerateRequestUuid());
        pivotCallService
            .getSaveFilterSetCall(dataSource, tpPivotFilterMapper).execute(params);
    }

    private Struct getStructTpPivotFilter(TpPivotFilter pivotFilter,
                                          IStructMapper<TpPivotFilter> tpPivotFilterStructMapper,
                                          DataSource dataSource) {
        try {
            var connection = getOracleConnection(dataSource);
            try {
                return tpPivotFilterStructMapper.toStruct(pivotFilter, connection);
            } finally {
                DataSourceUtils.releaseConnection(connection, dataSource);
            }
        } catch (SQLException e) {
            throw new ExecutingRuntimeException(e);
        }
    }

    @Override
    @Transactional
    public void deleteFilterSets(@NonNull String pivotName,
                                 @NonNull Long userId,
                                 List<String> filterNames) {
        var dataSource = pivotCallService.getDataSource();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(P_PIVOT_NAME, pivotName);
        params.addValue(P_USER_ID, userId);
        var array = getArrayFilterNames(filterNames, dataSource);
        params.addValue(P_NAMES, array);
        params.addValue(P_LOG_GROUP, getOrGenerateRequestUuid());
        pivotCallService.getDeleteFilterSetsCall(dataSource).execute(params);
    }

    private Array getArrayFilterNames(List<String> filterNames,
                                      DataSource dataSource) {
        try {
            var connection = getOracleConnection(dataSource);
            try {
                return stringListToArray(filterNames, connection);
            } finally {
                DataSourceUtils.releaseConnection(connection, dataSource);
            }
        } catch (SQLException e) {
            throw new ExecutingRuntimeException(e);
        }
    }

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public List<Object[]> getPivotData(@NonNull String pivotName,
                                       @NonNull Long userId,
                                       @NonNull Long startRow,
                                       @NonNull Long endRow) {

        return
            getPivotDataStoredProcedureQuery(
                pivotName,
                userId,
                startRow,
                endRow
            )
                .getResultList();
    }

    private StoredProcedureQuery getPivotDataStoredProcedureQuery(@NonNull String pivotName,
                                                                  @NonNull Long userId,
                                                                  @NonNull Long startRow,
                                                                  @NonNull Long endRow) {
        return getPivotDataStoredProcedureQuery(pivotName, userId, startRow, endRow, null);
    }

    private StoredProcedureQuery getPivotDataStoredProcedureQuery(@NonNull String pivotName,
                                                                  @NonNull Long userId,
                                                                  @NonNull Long startRow,
                                                                  @NonNull Long endRow,
                                                                  String collapsedOrderRows) {
        var query =
            pivotCallService.getPivotDataQuery()
                .setParameter(P_PIVOT_NAME, pivotName)
                .setParameter(P_USER_ID, userId)
                .setParameter(P_START_ROW, startRow)
                .setParameter(P_END_ROW, endRow)
                .setParameter(P_COLLAPSED_ORDER_ROWS, collapsedOrderRows)
                .setParameter(P_LOG_GROUP, getOrGenerateRequestUuid());
        query.execute();
        return query;
    }

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public List<String> getPivotDataString(@NonNull String pivotName,
                                           @NonNull Long userId,
                                           @NonNull Long startRow,
                                           @NonNull Long endRow) {
        return
            getPivotDataStoredProcedureQuery(
                pivotName,
                userId,
                startRow,
                endRow
            )
                .getResultList();
    }

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public List<JsonNode> getPivotDataJson(@NonNull String pivotName,
                                           @NonNull Long userId,
                                           @NonNull Long startRow,
                                           @NonNull Long endRow,
                                           String collapsedOrderRows) {
        return
            getPivotDataStoredProcedureQuery(
                pivotName,
                userId,
                startRow,
                endRow,
                collapsedOrderRows
            )
                .getResultList().stream()
                .map(row -> {
                    try {
                        return mapper.readValue(clobToString((Clob) row), JsonNode.class);
                    } catch (JsonProcessingException | SQLException e) {
                        throw new ExecutingRuntimeException(e);
                    }
                })
                .toList();
    }

    @Override
    @Transactional
    public List<Long> searchPivotRows(@NonNull String pivotName,
                                      @NonNull Long userId,
                                      @NonNull String searchText) {
        var query =
            pivotCallService.getSearchPivotRowsQuery()
                .setParameter(P_PIVOT_NAME, pivotName)
                .setParameter(P_USER_ID, userId)
                .setParameter(P_TEXT, searchText)
                .setParameter(P_LOG_GROUP, getOrGenerateRequestUuid());
        query.execute();
        try (ResultSet resultSet = (ResultSet) query.getOutputParameterValue(P_PIVOT_ROWS)) {
            if (isNull(resultSet)) {
                return Collections.emptyList();
            }
            var result = new ArrayList<Long>();
            while (resultSet.next()) {
                result.add(resultSet.getLong(1));
            }
            return result;
        } catch (SQLException e) {
            log.error("Metadata getting error {}", e.getMessage(), e);
            throw new ExecutingRuntimeException(e);
        }
    }

    @Override
    @Transactional
    public void downloadPivot(@NonNull PivotDownloadRequestDto request,
                              @NonNull Long userId,
                              @NonNull Consumer<PivotPreparingFileDto> updateStatus) {
        try {
            var query =
                pivotCallService.getDownloadPivotDataQuery()
                    .setParameter(P_PIVOT_NAME, request.getPivotId())
                    .setParameter(P_USER_ID, userId)
                    .setParameter(P_LOG_GROUP, getOrGenerateRequestUuid());
            query.execute();
            try (ResultSet resultSet = (ResultSet) query.getOutputParameterValue(P_PIVOT_DATA)) {
                if (isNull(resultSet)) {
                    return;
                }

                var config = getPivotConfigFromDatabase(request.getPivotId(), userId);
                var downloadService = getPivotExportDataService(request.getType());
                var data = downloadService.exportData(request, config, resultSet, updateStatus);
                updateStatus.accept(PivotPreparingFileDto.done(data));
            }
        } catch (SQLException | IOException | IllegalArgumentException e) {
            log.error("Task execution error {}", e.getMessage(), e);
            var exception = new ExecutingRuntimeException(e);
            updateStatus.accept(PivotPreparingFileDto.error(exception));
            throw exception;
        }
    }

    @Override
    @Transactional
    public BigDecimal getProgressPivot(@NonNull String pivotName,
                                       @NonNull Long userId) {
        var query =
            pivotCallService.getProgressPivotQuery()
                .setParameter(P_PIVOT_NAME, pivotName)
                .setParameter(P_USER_ID, userId)
                .setParameter(P_LOG_GROUP, getOrGenerateRequestUuid());
        query.execute();
        return (BigDecimal) query.getOutputParameterValue(P_PROGRESS);
    }

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public List<Object> getUniqFieldValues(@NonNull String pivotName,
                                           @NonNull String fieldName,
                                           @NonNull Long userId) {
        var query =
            pivotCallService.getPivotUniqFieldValuesQuery()
                .setParameter(P_PIVOT_NAME, pivotName)
                .setParameter(P_USER_ID, userId)
                .setParameter(P_FIELD, fieldName)
                .setParameter(P_LOG_GROUP, getOrGenerateRequestUuid());
        query.execute();
        return query.getResultList();
    }

    @SneakyThrows
    private PivotExportDataService getPivotExportDataService(DownloadType type) {
        return pivotExportDataServices.stream().filter(i -> i.isSupported(type)).findFirst()
            .orElseThrow(() -> new NotSupportedException("Unknown file format" + type.name()));
    }
}
