package ru.sportmaster.scd.exceptions;

public class AlgorithmExecutionProcessAlreadyRunningException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Currently, the algorithm execution process is already running. Try again later.";
    }
}
