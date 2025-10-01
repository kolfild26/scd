package ru.sportmaster.scd.repository.dictionary;

import java.util.List;
import ru.sportmaster.scd.entity.adjustment.Business;
import ru.sportmaster.scd.repository.AbstractRepository;

public interface BusinessRepository extends AbstractRepository<Business, Long> {
    List<Business> findNotDeletedBusinesses();
}
