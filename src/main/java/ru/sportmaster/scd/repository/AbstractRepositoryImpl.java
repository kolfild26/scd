package ru.sportmaster.scd.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class AbstractRepositoryImpl<T, I> implements AbstractRepository<T, I> {
    protected Class<T> clazz;

    @PersistenceContext
    protected EntityManager em;

    public AbstractRepositoryImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T findById(I id) {
        return em.find(clazz, id);
    }

    @Override
    public List<T> findAll() {
        return em.createQuery("select e from " + clazz.getName() + " e", clazz)
            .getResultList();
    }

    @Override
    public long count() {
        return (long) em.createQuery("select count(e) from " + clazz.getName() + " e")
            .getSingleResult();
    }

    @Override
    public T persist(T entity) {
        em.persist(entity);
        return entity;
    }

    @Override
    @Transactional
    public T merge(T entity) {
        return em.merge(entity);
    }

    @Override
    public T saveOrUpdate(T entity, I entityId) {
        if (entityId != null) {
            return em.merge(entity);
        } else {
            em.persist(entity);
            return entity;
        }
    }

    @Override
    @Transactional
    public void delete(T entity) {
        deleteEntity(entity);
    }

    private void deleteEntity(T entity) {
        em.remove(entity);
    }

    @Override
    @Transactional
    public void deleteById(I entityId) {
        T entity = findById(entityId);
        deleteEntity(entity);
    }

    @Override
    public void flush() {
        em.flush();
    }

}
