package ru.sportmaster.scd.exceptions;

public class FileBuildingNotCompletedException extends RuntimeException {
    @Override
    public String getMessage() {
        return "File building not completed.";
    }
}
