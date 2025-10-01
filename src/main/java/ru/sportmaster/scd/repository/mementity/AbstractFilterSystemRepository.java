package ru.sportmaster.scd.repository.mementity;

import jakarta.persistence.criteria.Predicate;
import org.springframework.lang.NonNull;
import ru.sportmaster.scd.repository.AbstractRepository;

public interface AbstractFilterSystemRepository<T, K> extends AbstractRepository<T, K> {
    Predicate getFilterPredicate(@NonNull Predicate predicate,
                                 @NonNull FilterSystemPredicateBuilder predicateBuilder);
}
