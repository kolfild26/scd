package ru.sportmaster.scd.repository.adjustment;

public interface AdjustmentDocRowValuesRepository {
    boolean hasColumnValue(Class<?> clazz, String path, Object value);

    <T> T getValue(Class<T> clazz, String path, Object value);
}
