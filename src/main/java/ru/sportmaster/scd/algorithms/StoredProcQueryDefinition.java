package ru.sportmaster.scd.algorithms;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import org.springframework.lang.NonNull;

/**
 * Класс формирования StoredProcedureQuery (вызов процедуры Oracle).
 */
@Builder
public class StoredProcQueryDefinition {
    private final String schemaName;
    private final String packageName;
    private final String procedureName;
    @Getter
    private final List<ParamDefinition> paramDefinitions;

    public StoredProcedureQuery getStoredProcedureQuery(EntityManager em) {
        var query = em.createStoredProcedureQuery(schemaName + '.' + packageName + '.' + procedureName);
        registerStoredProcedureParameters(query);
        return query;
    }

    private void registerStoredProcedureParameters(StoredProcedureQuery query) {
        for (var paramDefinition : paramDefinitions) {
            query.registerStoredProcedureParameter(
                paramDefinition.getParamName(),
                paramDefinition.getParamClass(),
                paramDefinition.getParameterMode()
            );
        }
    }

    public void setParamsValue(@NonNull StoredProcedureQuery query,
                               Map<String, Object> params) {
        paramDefinitions.stream()
            .filter(paramDefinition ->
                ParameterMode.IN.equals(paramDefinition.getParameterMode())
                    || ParameterMode.INOUT.equals(paramDefinition.getParameterMode()))
            .forEach(paramDefinition ->
                query.setParameter(
                    paramDefinition.getParamName(),
                    paramDefinition.getValue(params)
                )
            );
    }
}
