package ru.sportmaster.scd.exceptions;

/**
 * Ошибка отсутствия обязательного параметра во входных параметрах выполнения шага алгоритма.
 */
public class ParameterNotFoundException extends RuntimeException {
    private final String paramName;

    public ParameterNotFoundException(String paramName) {
        this.paramName = paramName;
    }

    @Override
    public String getMessage() {
        return String.format("Params %s not found in input parameters.", paramName);
    }
}
