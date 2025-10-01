package ru.sportmaster.scd.repository.dictionary;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;
import ru.sportmaster.scd.entity.dictionary.Collection;
import ru.sportmaster.scd.entity.dictionary.Collection_;
import ru.sportmaster.scd.repository.AbstractRepositoryImpl;

@Repository
public class CollectionRepositoryImpl
    extends AbstractRepositoryImpl<Collection, Long>
    implements CollectionRepository {
    public CollectionRepositoryImpl() {
        super(Collection.class);
    }

    @Override
    public List<Collection> findActualAfterYearCollections(LocalDate startPeriod) {
        var criteriaBuilder = em.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Collection.class);
        var root = query.from(Collection.class);

        var restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(
            criteriaBuilder.or(
                criteriaBuilder.isNull(root.get(Collection_.DATE_OPEN)),
                criteriaBuilder.greaterThanOrEqualTo(root.get(Collection_.DATE_OPEN), startPeriod)
            ),
            criteriaBuilder.and(
                restrictions,
                criteriaBuilder.isNull(root.get(Collection_.IS_DELETED)),
                criteriaBuilder.notEqual(root.get(Collection_.ID), 10000299)
            )
        );
        return em.createQuery(query.select(root).where(restrictions)).getResultList();
    }
}
