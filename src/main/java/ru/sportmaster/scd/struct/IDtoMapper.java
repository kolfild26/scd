package ru.sportmaster.scd.struct;

public interface IDtoMapper<T, D> {
    T fromDto(D source);

    D toDto(T source);
}
