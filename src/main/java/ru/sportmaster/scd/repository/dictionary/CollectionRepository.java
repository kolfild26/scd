package ru.sportmaster.scd.repository.dictionary;

import java.time.LocalDate;
import java.util.List;
import ru.sportmaster.scd.entity.dictionary.Collection;
import ru.sportmaster.scd.repository.AbstractRepository;

public interface CollectionRepository extends AbstractRepository<Collection, Long> {
    List<Collection> findActualAfterYearCollections(LocalDate startPeriod);
}
