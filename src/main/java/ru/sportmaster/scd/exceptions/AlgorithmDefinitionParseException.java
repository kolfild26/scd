package ru.sportmaster.scd.exceptions;

public class AlgorithmDefinitionParseException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Algorithm definition parse exception.";
    }
}
