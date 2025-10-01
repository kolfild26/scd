package ru.sportmaster.scd.repository.adjustment;

import static java.util.Objects.isNull;
import static ru.sportmaster.scd.utils.JpaUtil.getPath;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class AdjustmentDocRowValuesRepositoryImpl implements AdjustmentDocRowValuesRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public boolean hasColumnValue(Class<?> clazz, String path, Object value) {
        var criteriaBuilder = em.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Long.class);
        var root = query.from(clazz);
        query.select(criteriaBuilder.count(root));
        query.where(criteriaBuilder.equal(getPath(root, path), value));
        return em.createQuery(query).getSingleResult() != 0;
    }

    @Override
    public <T> T getValue(Class<T> clazz, String path, Object value) {
        if (isNull(value)) {
            return null;
        }

        var criteriaBuilder = em.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(clazz);
        var root = query.from(clazz);
        query.select(root);
        query.where(criteriaBuilder.equal(getPath(root, path), value));
        return em.createQuery(query).setMaxResults(1).getSingleResult();
    }
}
