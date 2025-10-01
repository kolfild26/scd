package ru.sportmaster.scd.mementity;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import org.springframework.data.domain.Sort;
import ru.sportmaster.scd.ui.view.type.ICondition;

public interface IMemEntityConditionBuilder<T> {
    Predicate<T> buildPredicates(List<ICondition> conditions);

    Comparator<T> buildOrder(Map<String, Sort.Direction> sort);
}
