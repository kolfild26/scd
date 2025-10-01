package ru.sportmaster.scd.repository.adjustment;

import ru.sportmaster.scd.entity.adjustment.AdjustmentUser;
import ru.sportmaster.scd.repository.AbstractRepository;

public interface AdjustmentUserRepository extends AbstractRepository<AdjustmentUser, Long> {
    Long getDcUserId(Long userId);
}
