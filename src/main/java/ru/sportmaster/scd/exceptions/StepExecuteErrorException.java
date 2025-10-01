package ru.sportmaster.scd.exceptions;

/**
 * Ошибка выполнения шага алгоритма.
 */
public class StepExecuteErrorException extends RuntimeException {
    private final Throwable throwable;
    private final String algorithmStepDefinition;

    public StepExecuteErrorException(Throwable throwable, String algorithmStepDefinition) {
        this.throwable = throwable;
        this.algorithmStepDefinition = algorithmStepDefinition;
    }

    @Override
    public String getMessage() {
        return String.format("Step %s execute error: %s.", algorithmStepDefinition, throwable.toString());
    }
}
