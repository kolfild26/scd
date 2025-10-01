package ru.sportmaster.scd.repository.adjustment;

import static java.lang.String.format;
import static java.util.Objects.isNull;
import static ru.sportmaster.scd.consts.ParamNames.DC_API_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.DOCUMENT;
import static ru.sportmaster.scd.consts.ParamNames.P_CORRECTION_DET_LIST;
import static ru.sportmaster.scd.consts.ParamNames.P_CORRECTION_DET_LIST_DEL;
import static ru.sportmaster.scd.consts.ParamNames.P_CORRECTION_DET_LIST_MERGE;
import static ru.sportmaster.scd.consts.ParamNames.P_DESCRIPTION;
import static ru.sportmaster.scd.consts.ParamNames.P_ERROR_CODE;
import static ru.sportmaster.scd.consts.ParamNames.P_ERROR_TEXT;
import static ru.sportmaster.scd.consts.ParamNames.P_ID_BUSINESS_TMA;
import static ru.sportmaster.scd.consts.ParamNames.P_ID_CORRECTION_LIST;
import static ru.sportmaster.scd.consts.ParamNames.P_ID_REF_USER;
import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.utils.StructUtil.getOracleConnection;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Struct;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.stereotype.Repository;
import ru.sportmaster.scd.entity.adjustment.AdjustmentDoc;
import ru.sportmaster.scd.entity.adjustment.AdjustmentDocType;
import ru.sportmaster.scd.entity.adjustment.AdjustmentDoc_;
import ru.sportmaster.scd.entity.adjustment.AdjustmentType;
import ru.sportmaster.scd.entity.adjustment.struct.ITpAdjustmentRow;
import ru.sportmaster.scd.exceptions.BadRequestException;
import ru.sportmaster.scd.exceptions.DataSourceNotFoundException;
import ru.sportmaster.scd.struct.IStructMapperFactory;

@Repository
@RequiredArgsConstructor
public class AdjustmentDocRowRepositoryImpl implements AdjustmentDocRowRepository {
    private final IStructMapperFactory structMapperFactory;
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Object> getAllRowsByDocumentId(Class<?> rowType, Long correctionId) {
        var criteriaBuilder = em.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Object.class);
        var root = query.from(rowType);

        var restrictions = criteriaBuilder.equal(root.get(DOCUMENT).get(AdjustmentDoc_.ID), correctionId);
        return em.createQuery(query.select(root).where(restrictions)).getResultList();
    }

    @Override
    public boolean hasRows(Class<?> rowType, Long correctionId) {
        var criteriaBuilder = em.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Long.class);
        var root = query.from(rowType);
        query.select(criteriaBuilder.count(root));

        var restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(
            restrictions,
            criteriaBuilder.equal(root.get(DOCUMENT).get(AdjustmentDoc_.ID), correctionId)
        );
        return em.createQuery(query.where(restrictions)).getSingleResult() != 0;
    }

    @Override
    public void create(Long correctionId, ITpAdjustmentRow row) {
        callUpdateFunction(correctionId, P_CORRECTION_DET_LIST_MERGE, List.of(row));
    }

    @Override
    public Long create(Long typeId, String description, Long businessId, Long userId, List<ITpAdjustmentRow> rows) {
        AdjustmentDocType docType = em.find(AdjustmentDocType.class, typeId);
        AdjustmentType type = docType.getType();
        String arrayType = format("scd.scd_dc_tp_%s_list", type.getDbType()).toUpperCase();

        var dataSource = getDataSource();
        var call = getCreateDocumentRowCall(dataSource, type, arrayType);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(P_DESCRIPTION, description);
        params.addValue(P_ID_BUSINESS_TMA, businessId);
        params.addValue(P_ID_REF_USER, userId);
        params.addValue(P_CORRECTION_DET_LIST, getArray(rows, arrayType, dataSource));

        Map<String, Object> result = call.execute(params);

        if (result.get(P_ERROR_CODE) != null) {
            String message = (String) result.get(P_ERROR_TEXT);
            throw new BadRequestException(message);
        }

        return ((BigDecimal) result.get(P_ID_CORRECTION_LIST)).longValue();
    }

    @Override
    public void update(Long correctionId, ITpAdjustmentRow row) {
        callUpdateFunction(correctionId, P_CORRECTION_DET_LIST_MERGE, List.of(row));
    }

    @Override
    public Long update(Long correctionId, List<ITpAdjustmentRow> rows) {
        return callUpdateFunction(correctionId, P_CORRECTION_DET_LIST_MERGE, rows);
    }

    @Override
    public void delete(Long correctionId, ITpAdjustmentRow row) {
        callUpdateFunction(correctionId, P_CORRECTION_DET_LIST_DEL, List.of(row));
    }

    private Long callUpdateFunction(Long correctionId, String paramName, List<ITpAdjustmentRow> rows) {
        AdjustmentDoc doc = em.find(AdjustmentDoc.class, correctionId);
        AdjustmentDocType docType = doc.getType();
        String arrayType = format("scd.scd_dc_tp_%s_list", docType.getType().getDbType()).toUpperCase();

        var dataSource = getDataSource();
        var call = getUpdateDocumentRowCall(dataSource, docType.getType(), arrayType);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(P_ID_CORRECTION_LIST, doc.getId());
        params.addValue(P_DESCRIPTION, doc.getComment());
        params.addValue(P_ID_REF_USER, doc.getUserOnSystem().getUser().getId());
        params.addValue(P_CORRECTION_DET_LIST_MERGE,
            getArray(P_CORRECTION_DET_LIST_MERGE.equalsIgnoreCase(paramName) ? rows : null, arrayType, dataSource)
        );
        params.addValue(P_CORRECTION_DET_LIST_DEL,
            getArray(P_CORRECTION_DET_LIST_DEL.equalsIgnoreCase(paramName) ? rows : null, arrayType, dataSource)
        );
        Map<String, Object> result = call.execute(params);

        if (result.get(P_ERROR_CODE) != null) {
            String message = (String) result.get(P_ERROR_TEXT);
            throw new BadRequestException(message);
        }

        return doc.getId();
    }

    @SneakyThrows
    private Array getArray(List<ITpAdjustmentRow> rows, String arrayType, DataSource dataSource) {
        var connection = getOracleConnection(dataSource);
        if (rows != null && !rows.isEmpty()) {
            Object[] array = rows.stream().map(row -> mapRow(row, connection)).toArray(Object[]::new);
            return connection.createOracleArray(arrayType, array);
        } else {
            return connection.createOracleArray(arrayType, new Object[0]);
        }
    }

    @SneakyThrows
    private Struct mapRow(ITpAdjustmentRow row, OracleConnection connection) {
        return structMapperFactory.getStructMapper(row.getClass()).objectToStruct(row, connection);
    }

    private SimpleJdbcCall getCreateDocumentRowCall(DataSource dataSource, AdjustmentType type, String arrayType) {
        return new SimpleJdbcCall(dataSource)
            .withSchemaName(SCD_SCHEMA_NAME)
            .withCatalogName(DC_API_PACKAGE_NAME)
            .withProcedureName("create_" + type.getDbType())
            .declareParameters(
                new SqlOutParameter(P_ID_CORRECTION_LIST, OracleTypes.NUMBER),
                new SqlParameter(P_DESCRIPTION, OracleTypes.VARCHAR),
                new SqlParameter(P_ID_BUSINESS_TMA, OracleTypes.NUMBER),
                new SqlParameter(P_ID_REF_USER, OracleTypes.NUMBER),
                new SqlParameter(P_CORRECTION_DET_LIST, OracleTypes.ARRAY, arrayType),
                new SqlOutParameter(P_ERROR_CODE, OracleTypes.NUMBER),
                new SqlOutParameter(P_ERROR_TEXT, OracleTypes.VARCHAR)
            );
    }

    private SimpleJdbcCall getUpdateDocumentRowCall(DataSource dataSource, AdjustmentType type, String arrayType) {
        return new SimpleJdbcCall(dataSource)
            .withSchemaName(SCD_SCHEMA_NAME)
            .withCatalogName(DC_API_PACKAGE_NAME)
            .withProcedureName("update_" + type.getDbType())
            .declareParameters(
                new SqlParameter(P_ID_CORRECTION_LIST, OracleTypes.NUMBER),
                new SqlParameter(P_DESCRIPTION, OracleTypes.VARCHAR),
                new SqlParameter(P_ID_REF_USER, OracleTypes.NUMBER),
                new SqlParameter(P_CORRECTION_DET_LIST_MERGE, OracleTypes.ARRAY, arrayType),
                new SqlParameter(P_CORRECTION_DET_LIST_DEL, OracleTypes.ARRAY, arrayType),
                new SqlOutParameter(P_ERROR_CODE, OracleTypes.NUMBER),
                new SqlOutParameter(P_ERROR_TEXT, OracleTypes.VARCHAR)
            );
    }

    private DataSource getDataSource() {
        var dataSource = ((EntityManagerFactoryInfo) em.getEntityManagerFactory()).getDataSource();
        if (dataSource instanceof HikariDataSource hikariDataSource) {
            dataSource = hikariDataSource.getDataSource();
        }
        if (isNull(dataSource)) {
            throw new DataSourceNotFoundException();
        }
        return dataSource;
    }
}
