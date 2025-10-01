package ru.sportmaster.scd.repository.dictionary;

import java.util.List;
import org.springframework.stereotype.Repository;
import ru.sportmaster.scd.entity.dictionary.ShopOSHierarchy;
import ru.sportmaster.scd.entity.dictionary.ShopOSHierarchy_;
import ru.sportmaster.scd.repository.AbstractRepositoryImpl;

@Repository
public class ShopOSHierarchyRepositoryImpl
    extends AbstractRepositoryImpl<ShopOSHierarchy, Long>
    implements ShopOSHierarchyRepository {
    public ShopOSHierarchyRepositoryImpl() {
        super(ShopOSHierarchy.class);
    }

    @Override
    public List<ShopOSHierarchy> findSortedForTree() {
        var criteriaBuilder = em.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(ShopOSHierarchy.class);
        var root = query.from(ShopOSHierarchy.class);

        var restrictions = criteriaBuilder.and(
            criteriaBuilder.isNotNull(root.get(ShopOSHierarchy_.MONIKER)),
            criteriaBuilder.isNull(root.get(ShopOSHierarchy_.IS_DELETED))
        );
        query.orderBy(criteriaBuilder.asc(root.get(ShopOSHierarchy_.LEVEL)));

        return em.createQuery(query.select(root).where(restrictions)).getResultList();
    }
}
