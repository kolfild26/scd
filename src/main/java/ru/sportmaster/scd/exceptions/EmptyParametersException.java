package ru.sportmaster.scd.exceptions;

/**
 * Ошибка отсутствия входных параметров выполнения шага алгоритма.
 */
public class EmptyParametersException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Params is empty.";
    }
}
