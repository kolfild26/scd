package ru.sportmaster.scd.repository.dictionary;

import java.util.List;
import ru.sportmaster.scd.entity.pivot.CountryGroup;
import ru.sportmaster.scd.repository.AbstractRepository;

public interface CountryGroupRepository extends AbstractRepository<CountryGroup, Long> {
    List<CountryGroup> findNotDeletedCountryGroups();
}
