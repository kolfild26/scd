package ru.sportmaster.scd.repository;

import javax.sql.DataSource;

public interface AbstractJdbcRepository {
    DataSource getDataSource();
}
