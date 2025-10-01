package ru.sportmaster.scd.repository.dictionary;

import java.util.List;
import ru.sportmaster.scd.entity.dictionary.ShopOSHierarchy;
import ru.sportmaster.scd.repository.AbstractRepository;

public interface ShopOSHierarchyRepository extends AbstractRepository<ShopOSHierarchy, Long> {
    List<ShopOSHierarchy> findSortedForTree();
}
