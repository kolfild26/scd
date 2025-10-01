package ru.sportmaster.scd.repository.dictionary;

import java.util.List;
import ru.sportmaster.scd.entity.dictionary.Partition;
import ru.sportmaster.scd.repository.AbstractRepository;

public interface PartitionRepository extends AbstractRepository<Partition, Long> {
    List<Partition> findByBusiness(Long idBusiness);
}
