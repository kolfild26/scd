package ru.sportmaster.scd.repository.dictionary;

import java.util.List;
import ru.sportmaster.scd.entity.dictionary.WareGroup;
import ru.sportmaster.scd.repository.AbstractRepository;

public interface WareGroupRepository extends AbstractRepository<WareGroup, Long> {
    List<WareGroup> findActualWareGroups();
}
