package ru.sportmaster.scd.exceptions;

public class FileBuildingNotCompleted extends RuntimeException {
    @Override
    public String getMessage() {
        return "File building not completed.";
    }
}
