package ru.sportmaster.scd.repository.dictionary;

import java.util.List;
import ru.sportmaster.scd.entity.dictionary.ShopSMHierarchy;
import ru.sportmaster.scd.repository.AbstractRepository;

public interface ShopSMHierarchyRepository extends AbstractRepository<ShopSMHierarchy, Long> {
    List<ShopSMHierarchy> findSortedForTree();
}
