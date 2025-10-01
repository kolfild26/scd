package ru.sportmaster.scd.service.view;

import static ru.sportmaster.scd.utils.JpaUtil.getTableName;

import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.mementity.MerchWareME;
import ru.sportmaster.scd.mementity.IMemEntityFilterSelBuilder;
import ru.sportmaster.scd.repository.mementity.AbstractFilterSystemRepository;
import ru.sportmaster.scd.repository.mementity.AbstractFilterSystemRepositoryImpl;
import ru.sportmaster.scd.repository.mementity.FilterSystemLogRepository;
import ru.sportmaster.scd.repository.mementity.FilterSystemPredicateBuilder;

@Component
public class MerchWareMEFilterSelect implements IMemEntityFilterSelBuilder {
    private static final Class<?> TYPE = MerchWareME.class;
    private final AbstractFilterSystemRepository<?, ?> filterSystemRepository;

    @Autowired
    public MerchWareMEFilterSelect(FilterSystemLogRepository filterSystemLogRepository) {
        filterSystemRepository = new AbstractFilterSystemRepositoryImpl<>(TYPE, filterSystemLogRepository);
    }

    @Override
    public String getType() {
        return TYPE.getSimpleName();
    }

    @Override
    public String getTabName() {
        return getTableName(TYPE);
    }

    @Override
    public Predicate buildPredicate(Predicate predicate,
                                    FilterSystemPredicateBuilder params) {
        return filterSystemRepository.getFilterPredicate(predicate, params);
    }
}
