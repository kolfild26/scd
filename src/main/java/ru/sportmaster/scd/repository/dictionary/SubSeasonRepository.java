package ru.sportmaster.scd.repository.dictionary;

import ru.sportmaster.scd.entity.replenishment.SubSeason;
import ru.sportmaster.scd.repository.AbstractRepository;

public interface SubSeasonRepository extends AbstractRepository<SubSeason, Long> {
    SubSeason getByName(String name);
}
