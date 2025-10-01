package ru.sportmaster.scd.exceptions;

public class QueueOperationError extends RuntimeException {
    @Override
    public String getMessage() {
        return "Queue operation error.";
    }
}
