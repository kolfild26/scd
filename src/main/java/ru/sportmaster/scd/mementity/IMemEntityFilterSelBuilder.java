package ru.sportmaster.scd.mementity;

import jakarta.persistence.criteria.Predicate;
import ru.sportmaster.scd.repository.mementity.FilterSystemPredicateBuilder;

public interface IMemEntityFilterSelBuilder {
    String getType();

    String getTabName();

    Predicate buildPredicate(Predicate predicate, FilterSystemPredicateBuilder params);
}
