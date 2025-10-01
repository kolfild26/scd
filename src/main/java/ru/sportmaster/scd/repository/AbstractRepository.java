package ru.sportmaster.scd.repository;

import java.util.List;

public interface AbstractRepository<T, I> {
    T findById(I id);

    long count();

    List<T> findAll();

    T persist(T entity);

    T merge(T entity);

    T saveOrUpdate(T entity, I entityId);

    void delete(T entity);

    void deleteById(I entityId);

    void flush();
}
