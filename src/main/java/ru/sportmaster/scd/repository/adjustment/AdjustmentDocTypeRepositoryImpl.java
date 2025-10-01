package ru.sportmaster.scd.repository.adjustment;

import org.springframework.stereotype.Repository;
import ru.sportmaster.scd.entity.adjustment.AdjustmentDocType;
import ru.sportmaster.scd.repository.AbstractRepositoryImpl;

@Repository
public class AdjustmentDocTypeRepositoryImpl extends AbstractRepositoryImpl<AdjustmentDocType, Long>
    implements AdjustmentDocTypeRepository {
    public AdjustmentDocTypeRepositoryImpl() {
        super(AdjustmentDocType.class);
    }
}
