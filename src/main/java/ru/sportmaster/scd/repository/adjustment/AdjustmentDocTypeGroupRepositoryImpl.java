package ru.sportmaster.scd.repository.adjustment;

import org.springframework.stereotype.Repository;
import ru.sportmaster.scd.entity.adjustment.AdjustmentDocTypeGroup;
import ru.sportmaster.scd.repository.AbstractRepositoryImpl;

@Repository
public class AdjustmentDocTypeGroupRepositoryImpl extends AbstractRepositoryImpl<AdjustmentDocTypeGroup, Long>
    implements AdjustmentDocTypeGroupRepository {
    public AdjustmentDocTypeGroupRepositoryImpl() {
        super(AdjustmentDocTypeGroup.class);
    }
}
