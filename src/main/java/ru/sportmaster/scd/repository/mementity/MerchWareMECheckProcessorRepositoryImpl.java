package ru.sportmaster.scd.repository.mementity;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Repository;
import ru.sportmaster.scd.entity.mementity.MerchWareME;
import ru.sportmaster.scd.mementity.MemEntityCheckProcessorRepositoryImpl;

@Repository
@ConditionalOnExpression("${scd.memory.entity.merch-use:true}")
public class MerchWareMECheckProcessorRepositoryImpl
    extends MemEntityCheckProcessorRepositoryImpl<Long>
    implements MerchWareMECheckProcessorRepository {

    public MerchWareMECheckProcessorRepositoryImpl() {
        super(MerchWareME.class);
    }

    @Override
    protected Long getLongKey(Long key) {
        return key;
    }
}
