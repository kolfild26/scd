package ru.sportmaster.scd.repository;

import static java.util.Objects.isNull;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import javax.sql.DataSource;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import ru.sportmaster.scd.exceptions.DataSourceNotFoundException;

public abstract class AbstractJdbcRepositoryImpl implements AbstractJdbcRepository {
    @PersistenceContext
    protected EntityManager em;

    @Override
    public DataSource getDataSource() {
        var dataSource = ((EntityManagerFactoryInfo) em.getEntityManagerFactory()).getDataSource();
        if (dataSource instanceof HikariDataSource hikariDataSource) {
            dataSource = hikariDataSource.getDataSource();
        }
        if (isNull(dataSource)) {
            throw new DataSourceNotFoundException();
        }
        return dataSource;
    }
}
