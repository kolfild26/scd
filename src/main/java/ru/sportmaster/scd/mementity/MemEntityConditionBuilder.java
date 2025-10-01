package ru.sportmaster.scd.mementity;

import static java.util.Comparator.comparing;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static ru.sportmaster.scd.consts.ParamNames.ID;
import static ru.sportmaster.scd.utils.JsonUtil.convertType;
import static ru.sportmaster.scd.utils.UiUtil.getFieldValue;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;
import org.springframework.data.domain.Sort;
import ru.sportmaster.scd.ui.view.type.ICondition;
import ru.sportmaster.scd.ui.view.type.UIViewCondition;
import ru.sportmaster.scd.ui.view.type.UIViewGroupCondition;

public class MemEntityConditionBuilder<T> implements IMemEntityConditionBuilder<T> {
    private final Comparator<T> defaultComparator = comparing(i -> getRowFieldValue(ID, i));
    @SuppressWarnings("rawtypes")
    private final Comparator nullFirstComparator = Comparator.nullsFirst(Comparator.naturalOrder());
    @SuppressWarnings("rawtypes")
    private final Comparator nullLastComparator = Comparator.nullsLast(Comparator.naturalOrder());

    @Override
    public Predicate<T> buildPredicates(List<ICondition> conditions) {
        if (conditions == null || conditions.isEmpty()) {
            return row -> true;
        }

        List<Predicate<T>> predicates = conditions.stream()
            .map(this::buildPredicate)
            .filter(Objects::nonNull)
            .toList();

        if (predicates.isEmpty()) {
            return row -> true;
        }

        return joinAnd(predicates);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Comparator<T> buildOrder(Map<String, Sort.Direction> sort) {
        if (sort == null || sort.isEmpty()) {
            return defaultComparator;
        } else {
            Comparator<T> comparator = null;
            for (Map.Entry<String, Sort.Direction> entry : sort.entrySet()) {
                String field = entry.getKey();
                Comparator<T> fieldComparator;
                if (entry.getValue().isAscending()) {
                    fieldComparator = comparing(i -> getRowFieldValue(field, i), nullFirstComparator);
                } else {
                    fieldComparator = comparing(i -> getRowFieldValue(field, i), nullLastComparator).reversed();
                }

                comparator = isNull(comparator) ? fieldComparator : comparator.thenComparing(fieldComparator);
            }

            return comparator;
        }
    }

    private Predicate<T> buildPredicate(ICondition condition) {
        if (condition == null) {
            return null;
        }

        if (condition.isGroup()) {
            return buildGroupCondition((UIViewGroupCondition) condition);
        } else {
            return buildCondition((UIViewCondition) condition);
        }
    }

    private Predicate<T> buildGroupCondition(UIViewGroupCondition group) {
        if (group.getConditions() == null || group.getConditions().isEmpty()) {
            return null;
        }

        List<Predicate<T>> predicates = group.getConditions().stream()
            .map(this::buildPredicate)
            .filter(Objects::nonNull)
            .toList();

        return switch (group.getJoin()) {
            case AND -> joinAnd(predicates);
            case OR -> joinOr(predicates);
        };
    }

    private Predicate<T> buildCondition(UIViewCondition condition) {
        return switch (condition.getOperation()) {
            case IN -> buildIn(condition);
            case NOT_IN -> buildIn(condition).negate();
            case EQ -> buildEq(condition);
            case NOT_EQ -> buildEq(condition).negate();
            case CONTAINS -> buildContains(condition);
            case IS_NULL -> buildIsNull(condition);
            case IS_NOT_NULL -> buildIsNull(condition).negate();
            case GT -> buildGreaterThan(condition);
            case LT -> buildLessThan(condition);
            case GTE -> buildGreaterThanEq(condition);
            case LTE -> buildLessThanEq(condition);
        };
    }

    private Predicate<T> buildIn(UIViewCondition condition) {
        List<Object> conditionValues = getConditionValueList(condition.getValue());
        return row -> {
            Object value = getRowFieldValue(condition.getField(), row);
            return conditionValues.contains(value);
        };
    }

    private Predicate<T> buildEq(UIViewCondition condition) {
        return row -> {
            Object fieldValue = getRowFieldValue(condition.getField(), row);
            if (isNull(fieldValue)) {
                return false;
            }
            Object conditionValue = convertType(fieldValue.getClass(), getConditionNode(condition.getValue()));
            return Objects.equals(fieldValue, conditionValue);
        };
    }

    private Predicate<T> buildContains(UIViewCondition condition) {
        String conditionValue = getConditionNode(condition.getValue()).asText();
        return row -> {
            String entityValue = ofNullable(getRowFieldValue(condition.getField(), row))
                .map(Object::toString)
                .orElse(null);
            return nonNull(entityValue) && entityValue.contains(conditionValue);
        };
    }

    private Predicate<T> buildIsNull(UIViewCondition condition) {
        return row -> isNull(getRowFieldValue(condition.getField(), row));
    }

    private Predicate<T> buildGreaterThan(UIViewCondition condition) {
        return row -> {
            Comparable<Object> fieldValue = getRowFieldValue(condition.getField(), row);
            if (isNull(fieldValue)) {
                return false;
            }
            return compareObjectAndNode(fieldValue, condition.getValue()) > 0;
        };
    }

    private Predicate<T> buildGreaterThanEq(UIViewCondition condition) {
        return row -> {
            Comparable<Object> fieldValue = getRowFieldValue(condition.getField(), row);
            if (isNull(fieldValue)) {
                return false;
            }
            return compareObjectAndNode(fieldValue, condition.getValue()) >= 0;
        };
    }

    private Predicate<T> buildLessThan(UIViewCondition condition) {
        return row -> {
            Comparable<Object> fieldValue = getRowFieldValue(condition.getField(), row);
            if (isNull(fieldValue)) {
                return false;
            }
            return compareObjectAndNode(fieldValue, condition.getValue()) < 0;
        };
    }

    private Predicate<T> buildLessThanEq(UIViewCondition condition) {
        return row -> {
            Comparable<Object> fieldValue = getRowFieldValue(condition.getField(), row);
            if (isNull(fieldValue)) {
                return false;
            }
            return compareObjectAndNode(fieldValue, condition.getValue()) <= 0;
        };
    }

    private Comparable<Object> getRowFieldValue(String path, Object entity) {
        if (entity == null || path == null) {
            return null;
        }
        if (entity instanceof IMemEntityGetter memEntity) {
            return memEntity.getFieldValue(path);
        }
        return getFieldValue(path, entity);
    }

    @SuppressWarnings("unchecked")
    private static int compareObjectAndNode(Comparable<Object> object, JsonNode node) {
        JsonNode conditionNode = getConditionNode(node);
        Comparable<Object> conditionValue = (Comparable<Object>) convertType(object.getClass(), conditionNode);
        return compareObjects(object, conditionValue);
    }

    private static int compareObjects(Comparable<? super Object> o1, Comparable<? super Object> o2) {
        if (isNull(o1) && isNull(o2)) {
            return 0;
        }
        if (isNull(o1)) {
            return 1;
        }
        if (isNull(o2)) {
            return -1;
        }
        return o1.compareTo(o2);
    }

    private static JsonNode getConditionNode(JsonNode node) {
        if (node.isNumber()) {
            return node;
        } else if (node.isObject() && node.has(ID)) {
            return node.get(ID);
        } else {
            return node;
        }
    }

    private static Object getConditionValue(JsonNode node) {
        if (node.isObject() && node.has(ID)) {
            return convertType(node.get(ID));
        }
        return convertType(node);
    }

    private static List<Object> getConditionValueList(JsonNode node) {
        return StreamSupport.stream(node.spliterator(), false)
            .map(MemEntityConditionBuilder::getConditionValue)
            .toList();
    }

    private Predicate<T> joinAnd(List<Predicate<T>> conditions) {
        return conditions.stream().reduce(Predicate::and).orElse(null);
    }

    private Predicate<T> joinOr(List<Predicate<T>> conditions) {
        return conditions.stream().reduce(Predicate::or).orElse(null);
    }
}
