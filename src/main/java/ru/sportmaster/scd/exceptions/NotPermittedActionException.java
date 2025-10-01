package ru.sportmaster.scd.exceptions;

public class NotPermittedActionException extends RuntimeException {
    public NotPermittedActionException(String message) {
        super(message);
    }
}
