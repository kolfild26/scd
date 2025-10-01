package ru.sportmaster.scd.algorithms;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static ru.sportmaster.scd.consts.ParamNames.ERROR_STEP_PARAM_NAME;
import static ru.sportmaster.scd.consts.ParamNames.NEXT_STEP_PARAM_NAME;
import static ru.sportmaster.scd.consts.ParamNames.P_SKIP_ERROR;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import java.sql.SQLException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.lang.NonNull;
import ru.sportmaster.scd.exceptions.EmptyParametersException;
import ru.sportmaster.scd.exceptions.ParameterNotFoundException;

/**
 * Шаг алгоритма, основанный на вызове процедуры Oracle.
 */
@Slf4j
public abstract class AbstractAlgorithmJdbcCallStep implements IExecutor {
    private static final String SKIP_ALL_ERROR = "ALL";
    private final String stepName;
    private final String stepDescribe;
    private final StoredProcQueryDefinition storedProcQueryDefinition;

    @PersistenceContext
    private EntityManager em;

    protected AbstractAlgorithmJdbcCallStep(
        @NonNull String stepName,
        @NonNull String stepDescribe,
        @NonNull StoredProcQueryDefinition storedProcQueryDefinition) {
        this.stepName = stepName;
        this.stepDescribe = stepDescribe;
        this.storedProcQueryDefinition = storedProcQueryDefinition;
    }

    @Override
    public String getName() {
        return stepName;
    }

    @Override
    public String getDescription() {
        return stepDescribe;
    }

    @Override
    public Map<String, Object> execute(Map<String, Object> params) {
        try {
            validateParams(params);
            var query = storedProcQueryDefinition.getStoredProcedureQuery(em);
            storedProcQueryDefinition.setParamsValue(query, params);
            query.execute();
            fillOutParams(query, params);
            processParams(params);
        } catch (Exception exception) {
            log.error("Error execution stored procedure^ {}", exception.getMessage(), exception);
            if (nonSkipError((String) params.get(P_SKIP_ERROR), exception)) {
                params.remove(NEXT_STEP_PARAM_NAME);
            }
            params.put(ERROR_STEP_PARAM_NAME, exception);
        }
        return params;
    }

    private void validateParams(Map<String, Object> params) {
        for (var paramName :
            storedProcQueryDefinition
                .getParamDefinitions()
                .stream()
                .filter(ParamDefinition::isRequired)
                .map(ParamDefinition::getParamName)
                .toList()) {
            if (isNull(params.get(paramName))) {
                throw new ParameterNotFoundException(paramName);
            }
        }
    }

    private void fillOutParams(StoredProcedureQuery query,
                               Map<String, Object> params) {
        storedProcQueryDefinition
            .getParamDefinitions()
            .stream()
            .filter(paramDefinition ->
                ParameterMode.INOUT.equals(paramDefinition.getParameterMode())
                    || ParameterMode.OUT.equals(paramDefinition.getParameterMode()))
            .forEach(paramDefinition -> fillOutParamValue(paramDefinition, query, params));
    }

    private void fillOutParamValue(ParamDefinition paramDefinition,
                                   StoredProcedureQuery query,
                                   Map<String, Object> params) {
        var value = query.getOutputParameterValue(paramDefinition.getParamName());
        if (nonNull(value)) {
            params.put(
                paramDefinition.getValueName(),
                paramDefinition.getParamClass().cast(value)
            );
        } else {
            params.remove(paramDefinition.getValueName());
        }
    }

    protected void processParams(Map<String, Object> params) {
        if (isNull(params)) {
            throw new EmptyParametersException();
        }
    }

    private boolean nonSkipError(String skipError,
                                 @NonNull Exception exception) {
        if (isNull(skipError)) {
            return true;
        }
        if (SKIP_ALL_ERROR.equalsIgnoreCase(skipError)) {
            return false;
        }
        int errorCode = 0;
        if (exception instanceof UncategorizedSQLException uncategorizedSQLException) {
            errorCode = uncategorizedSQLException.getSQLException().getErrorCode();
        } else if (exception instanceof SQLException sqlException) {
            errorCode = sqlException.getErrorCode();
        }
        if (errorCode == 0) {
            return true;
        }
        for (var skipErrorCodeDef : skipError.split(",")) {
            try {
                int skipErrorCode = Integer.parseInt(skipErrorCodeDef.trim());
                if (skipErrorCode == errorCode) {
                    return false;
                }
            } catch (NumberFormatException ignored) {
                // Передан неверный параметр, игнорируем
            }
        }
        return true;
    }
}
