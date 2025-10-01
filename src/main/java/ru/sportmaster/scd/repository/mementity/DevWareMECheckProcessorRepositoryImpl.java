package ru.sportmaster.scd.repository.mementity;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Repository;
import ru.sportmaster.scd.entity.mementity.DevWareME;
import ru.sportmaster.scd.mementity.MemEntityCheckProcessorRepositoryImpl;

@Repository
@ConditionalOnExpression("${scd.memory.entity.dev-use:true}")
public class DevWareMECheckProcessorRepositoryImpl
    extends MemEntityCheckProcessorRepositoryImpl<Long>
    implements DevWareMECheckProcessorRepository {

    public DevWareMECheckProcessorRepositoryImpl() {
        super(DevWareME.class);
    }

    @Override
    protected Long getLongKey(Long key) {
        return key;
    }
}
