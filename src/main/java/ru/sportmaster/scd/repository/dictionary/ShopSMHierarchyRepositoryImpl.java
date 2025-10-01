package ru.sportmaster.scd.repository.dictionary;

import java.util.List;
import org.springframework.stereotype.Repository;
import ru.sportmaster.scd.entity.dictionary.ShopSMHierarchy;
import ru.sportmaster.scd.entity.dictionary.ShopSMHierarchy_;
import ru.sportmaster.scd.repository.AbstractRepositoryImpl;

@Repository
public class ShopSMHierarchyRepositoryImpl
    extends AbstractRepositoryImpl<ShopSMHierarchy, Long>
    implements ShopSMHierarchyRepository {
    public ShopSMHierarchyRepositoryImpl() {
        super(ShopSMHierarchy.class);
    }

    @Override
    public List<ShopSMHierarchy> findSortedForTree() {
        var criteriaBuilder = em.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(ShopSMHierarchy.class);
        var root = query.from(ShopSMHierarchy.class);

        var restrictions = criteriaBuilder.and(
            criteriaBuilder.isNotNull(root.get(ShopSMHierarchy_.MONIKER)),
            criteriaBuilder.isNull(root.get(ShopSMHierarchy_.IS_DELETED))
        );
        query.orderBy(criteriaBuilder.asc(root.get(ShopSMHierarchy_.LEVEL)));

        return em.createQuery(query.select(root).where(restrictions)).getResultList();
    }
}
