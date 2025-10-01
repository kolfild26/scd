package ru.sportmaster.scd.repository.dictionary;

import java.util.List;
import ru.sportmaster.scd.entity.dictionary.CategoryHierarchy;
import ru.sportmaster.scd.repository.AbstractRepository;

public interface CategoryHierarchyRepository extends AbstractRepository<CategoryHierarchy, Long> {
    List<CategoryHierarchy> findNotDeletedCategories();
}
