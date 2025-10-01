package ru.sportmaster.scd.repository.dictionary;

import java.util.List;
import org.springframework.stereotype.Repository;
import ru.sportmaster.scd.entity.dictionary.WareGroup;
import ru.sportmaster.scd.entity.dictionary.WareGroup_;
import ru.sportmaster.scd.repository.AbstractRepositoryImpl;

@Repository
public class WareGroupRepositoryImpl extends AbstractRepositoryImpl<WareGroup, Long> implements WareGroupRepository {
    public WareGroupRepositoryImpl() {
        super(WareGroup.class);
    }

    @Override
    public List<WareGroup> findActualWareGroups() {
        var criteriaBuilder = em.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(WareGroup.class);
        var root = query.from(WareGroup.class);

        var restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(
            restrictions,
            criteriaBuilder.between(root.get(WareGroup_.ID), 1, 4)
        );
        return em.createQuery(query.select(root).where(restrictions)).getResultList();
    }
}
