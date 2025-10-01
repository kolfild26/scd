package ru.sportmaster.scd.exceptions;

public class DataSourceNotFoundException  extends RuntimeException {
    @Override
    public String getMessage() {
        return "DataSource not found.";
    }
}
