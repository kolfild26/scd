package ru.sportmaster.scd.repository.dictionary;

import java.util.List;
import org.springframework.stereotype.Repository;
import ru.sportmaster.scd.entity.dictionary.CategoryHierarchy;
import ru.sportmaster.scd.entity.dictionary.CategoryHierarchy_;
import ru.sportmaster.scd.repository.AbstractRepositoryImpl;

@Repository
public class CategoryHierarchyRepositoryImpl extends AbstractRepositoryImpl<CategoryHierarchy, Long>
    implements CategoryHierarchyRepository {
    public CategoryHierarchyRepositoryImpl() {
        super(CategoryHierarchy.class);
    }

    @Override
    public List<CategoryHierarchy> findNotDeletedCategories() {
        var criteriaBuilder = em.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(CategoryHierarchy.class);
        var root = query.from(CategoryHierarchy.class);

        var restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(
            restrictions,
            criteriaBuilder.isNull(root.get(CategoryHierarchy_.IS_DELETED))
        );
        return em.createQuery(query.select(root).where(restrictions)).getResultList();
    }
}
