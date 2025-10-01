package ru.sportmaster.scd.algorithms;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import jakarta.persistence.ParameterMode;
import java.util.Map;
import java.util.function.UnaryOperator;
import lombok.Getter;
import ru.sportmaster.scd.exceptions.ParameterNotFoundException;

/**
 * Описание параметра.
 */
@Getter
public class ParamDefinition {
    private final String paramName;
    private final Class<?> paramClass;
    private final ParameterMode parameterMode;
    private final boolean isRequired;
    private final UnaryOperator<Object> valueConverter;
    private final String valueName;

    public ParamDefinition(String paramName,
                           Class<?> paramClass,
                           UnaryOperator<Object> valueConverter) {
        this.paramName = paramName;
        this.paramClass = paramClass;
        this.parameterMode = ParameterMode.IN;
        this.isRequired = false;
        this.valueConverter = valueConverter;
        this.valueName = this.paramName;
    }

    public ParamDefinition(String paramName,
                           Class<?> paramClass,
                           UnaryOperator<Object> valueConverter,
                           boolean isRequired) {
        this.paramName = paramName;
        this.paramClass = paramClass;
        this.parameterMode = ParameterMode.IN;
        this.isRequired = isRequired;
        this.valueConverter = valueConverter;
        this.valueName = this.paramName;
    }

    public ParamDefinition(String paramName,
                           Class<?> paramClass,
                           String valueName) {
        this.paramName = paramName;
        this.paramClass = paramClass;
        this.parameterMode = ParameterMode.IN;
        this.isRequired = false;
        this.valueConverter = null;
        this.valueName = valueName;
    }

    public ParamDefinition(String paramName,
                           Class<?> paramClass,
                           ParameterMode parameterMode,
                           UnaryOperator<Object> valueConverter) {
        this.paramName = paramName;
        this.paramClass = paramClass;
        this.parameterMode = parameterMode;
        this.isRequired = false;
        this.valueConverter = valueConverter;
        this.valueName = this.paramName;
    }

    /**
     * Получение значения параметра из входных параметров выполнения процедуры Oracle.
     * @param params - входные параметры выполнения процедуры Oracle
     * @return - значение параметра
     */
    public Object getValue(Map<String, Object> params) {
        if (isNull(params) || !params.containsKey(valueName)) {
            if (isRequired) {
                throw new ParameterNotFoundException(paramName);
            }
            return null;
        }
        if (nonNull(valueConverter)) {
            return valueConverter.apply(params.get(valueName));
        }
        return params.get(valueName);
    }
}
