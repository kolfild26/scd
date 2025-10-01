package ru.sportmaster.scd.repository.planlimit;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import java.util.List;
import org.springframework.stereotype.Repository;
import ru.sportmaster.scd.entity.dictionary.Collection;
import ru.sportmaster.scd.entity.dictionary.Collection_;
import ru.sportmaster.scd.entity.dictionary.DivisionSCD_;
import ru.sportmaster.scd.entity.planlimit.PlPriorityMatrix;
import ru.sportmaster.scd.entity.planlimit.PlPriorityMatrix_;
import ru.sportmaster.scd.repository.AbstractRepositoryImpl;

@Repository
public class PlanLimitSettingsRepositoryImpl
    extends AbstractRepositoryImpl<PlPriorityMatrix, Long>
    implements PlanLimitSettingsRepository {
    public PlanLimitSettingsRepositoryImpl() {
        super(PlPriorityMatrix.class);
    }

    @Override
    public List<Collection> getExistingCollections(Long divisionId) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Collection> query = builder.createQuery(Collection.class);
        Root<Collection> root = query.from(Collection.class);

        Subquery<Long> subquery = query.subquery(Long.class);
        Root<PlPriorityMatrix> subqueryRoot = subquery.from(PlPriorityMatrix.class);
        subquery.select(subqueryRoot.get(PlPriorityMatrix_.COLLECTION).get(Collection_.ID)).distinct(true);
        subquery.where(builder.equal(subqueryRoot.get(PlPriorityMatrix_.DIVISION).get(DivisionSCD_.ID), divisionId));

        Predicate predicate = builder.conjunction();
        predicate = builder.and(predicate, builder.in(root.get(Collection_.ID)).value(subquery));
        query.orderBy(builder.asc(root.get(Collection_.ID)));

        return em.createQuery(query.select(root).where(predicate)).getResultList();
    }
}
