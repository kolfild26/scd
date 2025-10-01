package ru.sportmaster.scd.repository.dictionary;

import java.util.List;
import org.springframework.stereotype.Repository;
import ru.sportmaster.scd.entity.adjustment.Business;
import ru.sportmaster.scd.entity.adjustment.Business_;
import ru.sportmaster.scd.repository.AbstractRepositoryImpl;

@Repository
public class BusinessRepositoryImpl extends AbstractRepositoryImpl<Business, Long> implements BusinessRepository {
    public BusinessRepositoryImpl() {
        super(Business.class);
    }

    @Override
    public List<Business> findNotDeletedBusinesses() {
        var criteriaBuilder = em.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Business.class);
        var root = query.from(Business.class);

        var restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(
            restrictions,
            criteriaBuilder.isNull(root.get(Business_.IS_DELETED))
        );
        query.orderBy(criteriaBuilder.desc(root.get(Business_.ID)));
        return em.createQuery(query.select(root).where(restrictions)).getResultList();
    }
}
