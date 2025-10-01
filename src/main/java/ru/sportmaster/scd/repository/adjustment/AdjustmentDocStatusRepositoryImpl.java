package ru.sportmaster.scd.repository.adjustment;

import org.springframework.stereotype.Repository;
import ru.sportmaster.scd.entity.adjustment.AdjustmentDocStatus;
import ru.sportmaster.scd.repository.AbstractRepositoryImpl;

@Repository
public class AdjustmentDocStatusRepositoryImpl extends AbstractRepositoryImpl<AdjustmentDocStatus, Long>
    implements AdjustmentDocStatusRepository {
    public AdjustmentDocStatusRepositoryImpl() {
        super(AdjustmentDocStatus.class);
    }
}
