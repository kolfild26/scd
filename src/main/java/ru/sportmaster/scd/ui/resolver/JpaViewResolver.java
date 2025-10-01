package ru.sportmaster.scd.ui.resolver;

import static java.util.Objects.nonNull;
import static ru.sportmaster.scd.consts.ParamNames.ID;
import static ru.sportmaster.scd.consts.ParamNames.IS_DELETED;
import static ru.sportmaster.scd.utils.JpaUtil.buildJoinsOrFetches;
import static ru.sportmaster.scd.utils.JpaUtil.convertDateFieldToString;
import static ru.sportmaster.scd.utils.JpaUtil.getPath;
import static ru.sportmaster.scd.utils.JpaUtil.isEmbeddable;
import static ru.sportmaster.scd.utils.JsonUtil.convertTupleToObjectNode;
import static ru.sportmaster.scd.utils.UiUtil.DB_TO_API_DATE_SHORT_FORMAT;
import static ru.sportmaster.scd.utils.UiUtil.isDateOrTime;
import static ru.sportmaster.scd.utils.UiUtil.isDayOrWeek;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.dto.view.UiViewSearchRequestDto;
import ru.sportmaster.scd.dto.view.UiViewSearchResponseDto;
import ru.sportmaster.scd.mementity.IMemEntityFilterSelBuilder;
import ru.sportmaster.scd.repository.mementity.FilterSystemPredicateBuilder;
import ru.sportmaster.scd.ui.view.IUiViewSearchProcessor;
import ru.sportmaster.scd.ui.view.UiViewManager;
import ru.sportmaster.scd.ui.view.UiViewResolver;
import ru.sportmaster.scd.ui.view.type.ICondition;
import ru.sportmaster.scd.ui.view.type.UIViewCondition;
import ru.sportmaster.scd.ui.view.type.UIViewFetchFieldRequest;
import ru.sportmaster.scd.ui.view.type.UIViewFetchFieldValuesRequest;
import ru.sportmaster.scd.ui.view.type.UIViewGroupCondition;
import ru.sportmaster.scd.ui.view.type.UIViewMemSelection;
import ru.sportmaster.scd.ui.view.type.UiView;
import ru.sportmaster.scd.ui.view.type.UiViewFetchRequest;

@Component
@RequiredArgsConstructor
public class JpaViewResolver implements UiViewResolver, IUiViewSearchProcessor {
    private final ObjectMapper objectMapper;
    private final UiViewManager viewManager;
    private final JpaViewSearchProcessor jpaViewSearchProcessor;

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<ObjectNode> findAll(UiViewFetchRequest request, String sessionUuid, String formUuid) {
        UiView view = viewManager.getView(request.getView());
        boolean hideDeleted = view.isDeletable() && !view.isRecoverable();

        List<ObjectNode> result = prepareTypedQuery(view, request, hideDeleted)
            .getResultStream()
            .map(i -> objectMapper.convertValue(i, ObjectNode.class))
            .toList();

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        long total = getTotal(view.getJavaType(), request, hideDeleted);
        return new PageImpl<>(result, pageable, total);
    }

    @Override
    public Stream<?> findAllStream(UiViewFetchRequest request, String sessionUuid, String formUuid) {
        UiView view = viewManager.getView(request.getView());
        boolean hideDeleted = view.isDeletable() && !view.isRecoverable();

        return prepareTypedQuery(view, request, hideDeleted).getResultStream();
    }

    @Override
    public Page<ObjectNode> findAllOnlyFields(UIViewFetchFieldRequest request) {
        UiView view = viewManager.getView(request.getView());
        boolean hideDeleted = view.isDeletable() && !view.isRecoverable();
        CriteriaQuery<Tuple> query = buildRequestOnlyFields(
            view.getJavaType(), view.getIdField(), request, hideDeleted
        );

        TypedQuery<Tuple> typedQuery = em.createQuery(query);
        if (!request.isFull()) {
            typedQuery = typedQuery
                .setFirstResult((int) request.getOffset())
                .setMaxResults(request.getSize());
        }
        List<ObjectNode> result = typedQuery
            .getResultStream()
            .map(i -> convertTupleToObjectNode(i, request.getFields()))
            .toList();

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        long total = getTotal(view.getJavaType(), request, hideDeleted);
        return new PageImpl<>(result, pageable, total);
    }

    @Override
    public Page<Object> findAllIds(UiViewFetchRequest request) {
        UiView view = viewManager.getView(request.getView());
        boolean hideDeleted = view.isDeletable() && !view.isRecoverable();
        CriteriaQuery<Object> query = buildIdsRequest(view.getJavaType(), view.getIdField(), request, hideDeleted);

        TypedQuery<Object> typedQuery = em.createQuery(query);
        if (!request.isFull()) {
            typedQuery = typedQuery
                .setFirstResult((int) request.getOffset())
                .setMaxResults(request.getSize());
        }
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        long total = getTotal(view.getJavaType(), request, hideDeleted);
        return new PageImpl<>(typedQuery.getResultList(), pageable, total);
    }

    @Override
    public Object findOne(UiViewFetchRequest request) {
        UiView view = viewManager.getView(request.getView());
        boolean hideDeleted = view.isDeletable() && !view.isRecoverable();

        CriteriaQuery<?> query = buildRequest(view.getJavaType(), view.getIdField(), request, hideDeleted);
        TypedQuery<?> typedQuery = em.createQuery(query);
        typedQuery.setMaxResults(1);
        return typedQuery.getResultStream().findFirst().orElse(null);
    }

    @Override
    public List<?> findFieldValuesByPath(UIViewFetchFieldValuesRequest request, String sessionUuid, String formUuid) {
        UiView view = viewManager.getView(request.getView());
        boolean hideDeleted = view.isDeletable() && !view.isRecoverable();
        CriteriaQuery<?> query = buildFieldValuesRequest(view.getJavaType(), request.getPath(), request, hideDeleted);

        TypedQuery<?> typedQuery = em.createQuery(query);
        if (!request.isFull()) {
            typedQuery = typedQuery
                .setFirstResult((int) request.getOffset())
                .setMaxResults(request.getSize());
        }

        return typedQuery.getResultList();
    }

    @Override
    public void clear(String formUuid) {
        throw new NotImplementedException("clear(formUuid) not implemented");
    }

    @Override
    public UiViewSearchResponseDto pageSearch(UiViewSearchRequestDto request, Long userId) {
        return jpaViewSearchProcessor.pageSearch(request, userId);
    }

    @Override
    public int getSearchItemPosition(String view, Long userId, int currentPosition, Byte direction) {
        return jpaViewSearchProcessor.getSearchItemPosition(view, userId, currentPosition, direction);
    }

    @Override
    public void clearSearch(String view, Long userId) {
        jpaViewSearchProcessor.clearSearch(view, userId);
    }

    private TypedQuery<?> prepareTypedQuery(UiView view, UiViewFetchRequest request, boolean hideDeleted) {
        CriteriaQuery<?> query = buildRequest(view.getJavaType(), view.getIdField(), request, hideDeleted);

        TypedQuery<?> typedQuery = em.createQuery(query);
        if (!request.isFull()) {
            typedQuery = typedQuery
                .setFirstResult((int) request.getOffset())
                .setMaxResults(request.getSize());
        }

        return typedQuery;
    }

    private <T> CriteriaQuery<T> buildRequest(
        Class<T> clazz, String idField, UiViewFetchRequest request, boolean isDeletable
    ) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<T> query = criteriaBuilder.createQuery(clazz);
        Root<T> root = query.from(clazz);

        query.orderBy(buildOrderList(root, idField, criteriaBuilder, request.getSort(), true));

        return query.select(root).distinct(true)
            .where(buildPredicates(criteriaBuilder, query, root, request, isDeletable));
    }

    private <T> CriteriaQuery<Tuple> buildRequestOnlyFields(
        Class<T> clazz, String idField, UIViewFetchFieldRequest request, boolean isDeletable
    ) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = criteriaBuilder.createTupleQuery();
        Root<T> root = query.from(clazz);
        query.orderBy(buildOrderList(root, idField, criteriaBuilder, request.getSort(), true));

        List<Selection<?>> paths = new ArrayList<>();
        request.getFields().forEach(field -> paths.add(getPath(root, field)));

        return query.multiselect(paths).distinct(true)
            .where(buildPredicates(criteriaBuilder, query, root, request, isDeletable));
    }

    private CriteriaQuery<Object> buildIdsRequest(
        Class<?> clazz, String idField, UiViewFetchRequest request, boolean isDeletable
    ) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Object> query = criteriaBuilder.createQuery(Object.class);
        Root<?> root = query.from(clazz);

        Predicate predicate = buildPredicates(criteriaBuilder, query, root, request, isDeletable);
        query.orderBy(buildOrderList(root, idField, criteriaBuilder, request.getSort(), false));

        return query.select(root.get(idField)).where(predicate);
    }

    private CriteriaQuery<?> buildFieldValuesRequest(
        Class<?> clazz, String path, UIViewFetchFieldValuesRequest request, boolean isDeletable
    ) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<?> query = criteriaBuilder.createQuery(Object.class);
        Root<?> root = query.from(clazz);
        Path<?> fieldPath = getPath(root, path);

        Predicate predicate = buildPredicates(criteriaBuilder, query, root, request, isDeletable);
        predicate = criteriaBuilder.and(predicate, buildPathValuePredicate(criteriaBuilder, fieldPath, request));
        predicate = criteriaBuilder.and(predicate, criteriaBuilder.isNotNull(fieldPath));
        query.orderBy(criteriaBuilder.asc(fieldPath));

        return query.select(getPath(root, path)).distinct(true).where(predicate);
    }

    private <T> Long getTotal(Class<T> clazz, UiViewFetchRequest request, boolean isDeletable) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<T> root = query.from(clazz);

        return em.createQuery(
            query.select(criteriaBuilder.count(root))
                .where(buildPredicates(criteriaBuilder, query, root, request, isDeletable))
        ).getSingleResult();
    }

    public Predicate buildPredicates(CriteriaBuilder builder, CriteriaQuery<?> query, Root<?> root,
                                     UiViewFetchRequest request, boolean isDeletable) {
        Predicate restrictions = builder.conjunction();
        List<ICondition> conditions = request.getConditions();
        List<UIViewMemSelection> memSelections = request.getMemSelections();

        if (conditions != null && !conditions.isEmpty()) {
            List<Predicate> predicates = conditions.stream()
                .map(condition -> buildPredicate(builder, root, condition))
                .filter(Objects::nonNull)
                .toList();

            for (Predicate predicate : predicates) {
                restrictions = builder.and(restrictions, predicate);
            }
        }

        if (isDeletable) {
            restrictions = builder.and(restrictions, builder.isNull(root.get(IS_DELETED)));
        }

        if (nonNull(memSelections) && !memSelections.isEmpty()) {
            for (UIViewMemSelection selection : memSelections) {
                IMemEntityFilterSelBuilder filterBuilder = viewManager.getMemSelection(selection.getView());
                restrictions =
                    filterBuilder.buildPredicate(
                        restrictions,
                        FilterSystemPredicateBuilder.builder()
                            .formUUID(selection.getFormUuid())
                            .tableName(filterBuilder.getTabName())
                            .criteriaBuilder(builder)
                            .query(query)
                            .filteringIdExpression(root.get(selection.getField()))
                            .build()
                    );
            }
        }
        return restrictions;
    }

    public static List<Order> buildOrderList(
        Root<?> root, String idField, CriteriaBuilder builder, Map<String, Sort.Direction> sort, boolean distinct
    ) {
        List<Order> result = new ArrayList<>();

        if (sort != null) {
            sort.forEach((field, direction) -> {
                buildJoinsOrFetches(root, field, distinct);

                if (direction.isAscending()) {
                    result.add(builder.asc(getPath(root, field)));
                } else {
                    result.add(builder.desc(getPath(root, field)));
                }

            });
        }

        if (nonNull(idField) && (sort == null || !sort.containsKey(idField))) {
            result.add(builder.asc(root.get(idField)));
        }

        return result;
    }

    private static Predicate buildPredicate(CriteriaBuilder builder, Root<?> root, ICondition condition) {
        if (condition == null) {
            return null;
        }

        if (condition.isGroup()) {
            return buildGroupCondition(builder, root, (UIViewGroupCondition) condition);
        } else {
            return buildCondition(builder, root, (UIViewCondition) condition);
        }
    }

    private static Predicate buildGroupCondition(CriteriaBuilder builder, Root<?> root, UIViewGroupCondition group) {
        if (group.getConditions() == null || group.getConditions().isEmpty()) {
            return null;
        }

        Predicate[] predicates = group.getConditions().stream()
            .map(condition -> buildPredicate(builder, root, condition))
            .filter(Objects::nonNull)
            .toArray(Predicate[]::new);

        return switch (group.getJoin()) {
            case AND -> builder.and(predicates);
            case OR -> builder.or(predicates);
        };
    }

    private static Predicate buildCondition(CriteriaBuilder builder, Root<?> root, UIViewCondition condition) {
        return switch (condition.getOperation()) {
            case IN -> buildIn(root, builder, condition);
            case NOT_IN -> buildNotIn(root, builder, condition);
            case EQ -> buildEq(root, builder, condition);
            case NOT_EQ -> buildNotEq(root, builder, condition);
            case CONTAINS -> buildContains(root, builder, condition);
            case IS_NULL -> buildIsNull(root, builder, condition);
            case IS_NOT_NULL -> buildIsNotNull(root, builder, condition);
            case GT -> buildGreaterThan(root, builder, condition);
            case LT -> buildLessThan(root, builder, condition);
            case GTE -> buildGreaterThanEq(root, builder, condition);
            case LTE -> buildLessThanEq(root, builder, condition);
        };
    }

    private static Predicate buildIn(Root<?> root, CriteriaBuilder builder, UIViewCondition condition) {
        Path<?> path = buildPath(root, condition);
        var values = StreamSupport.stream(condition.getValue().spliterator(), false)
            .map(JpaViewResolver::getValue)
            .toList();
        if (isDateOrTime(path.getJavaType()) || isDayOrWeek(path.getJavaType())) {
            return convertDateFieldToString(path.getJavaType(), builder, path).in(values);
        } else {
            return path.in(values);
        }
    }

    private static Predicate buildNotIn(Root<?> root, CriteriaBuilder builder, UIViewCondition condition) {
        Path<?> path = buildPath(root, condition);
        var values = StreamSupport.stream(condition.getValue().spliterator(), false)
            .map(JpaViewResolver::getValue)
            .toList();
        if (isDateOrTime(path.getJavaType()) || isDayOrWeek(path.getJavaType())) {
            return convertDateFieldToString(path.getJavaType(), builder, path).in(values).not();
        } else {
            return path.in(values).not();
        }
    }

    private static Predicate buildEq(Root<?> root, CriteriaBuilder builder, UIViewCondition condition) {
        Path<?> path = buildPath(root, condition);
        if (isDateOrTime(path.getJavaType()) || isDayOrWeek(path.getJavaType())) {
            return builder.equal(
                convertDateFieldToString(path.getJavaType(), builder, path),
                getValue(condition.getValue())
            );
        } else {
            return builder.equal(path, getValue(condition.getValue()));
        }
    }

    private static Predicate buildNotEq(Root<?> root, CriteriaBuilder builder, UIViewCondition condition) {
        Path<?> path = buildPath(root, condition);
        if (isDateOrTime(path.getJavaType()) || isDayOrWeek(path.getJavaType())) {
            return builder.notEqual(
                convertDateFieldToString(path.getJavaType(), builder, path),
                getValue(condition.getValue())
            );
        } else {
            return builder.notEqual(path, getValue(condition.getValue()));
        }
    }

    private static Predicate buildContains(Root<?> root, CriteriaBuilder builder, UIViewCondition condition) {
        String template = "%" + condition.getValue().asText().toLowerCase() + "%";
        Path<?> path = buildPath(root, condition);
        if (isDateOrTime(path.getJavaType()) || isDayOrWeek(path.getJavaType())) {
            return builder.like(convertDateFieldToString(path.getJavaType(), builder, path), template);
        } else {
            return builder.like(builder.lower(path.as(String.class)), template);
        }
    }

    private static Predicate buildIsNull(Root<?> root, CriteriaBuilder builder, UIViewCondition condition) {
        return builder.isNull(buildPath(root, condition));
    }

    private static Predicate buildIsNotNull(Root<?> root, CriteriaBuilder builder, UIViewCondition condition) {
        return builder.isNotNull(buildPath(root, condition));
    }

    private static Predicate buildGreaterThan(Root<?> root, CriteriaBuilder builder, UIViewCondition condition) {
        Path<? extends Comparable> path = buildPath(root, condition);
        if (isDateOrTime(path.getJavaType()) || isDayOrWeek(path.getJavaType())) {
            return buildDateCondition(builder, condition, LocalDate.class, path, builder::greaterThan);
        } else {
            return builder.greaterThan(path, getValue(condition.getValue()));
        }
    }

    private static Predicate buildGreaterThanEq(Root<?> root, CriteriaBuilder builder, UIViewCondition condition) {
        Path<? extends Comparable> path = buildPath(root, condition);
        if (isDateOrTime(path.getJavaType()) || isDayOrWeek(path.getJavaType())) {
            return buildDateCondition(builder, condition, LocalDate.class, path, builder::greaterThanOrEqualTo);
        } else if (!isEmbeddable(path.getJavaType())) {
            return builder.greaterThanOrEqualTo(path, getValue(condition.getValue()));
        } else {
            return null;
        }
    }

    private static Predicate buildLessThan(Root<?> root, CriteriaBuilder builder, UIViewCondition condition) {
        Path<? extends Comparable> path = buildPath(root, condition);
        if (isDateOrTime(path.getJavaType()) || isDayOrWeek(path.getJavaType())) {
            return buildDateCondition(builder, condition, LocalDate.class, path, builder::lessThan);
        } else {
            return builder.lessThan(path, getValue(condition.getValue()));
        }
    }

    private static Predicate buildLessThanEq(Root<?> root, CriteriaBuilder builder, UIViewCondition condition) {
        Path<? extends Comparable> path = buildPath(root, condition);
        if (isDateOrTime(path.getJavaType()) || isDayOrWeek(path.getJavaType())) {
            return buildDateCondition(builder, condition, LocalDate.class, path, builder::lessThanOrEqualTo);
        } else {
            return builder.lessThanOrEqualTo(path, getValue(condition.getValue()));
        }
    }

    private static <T extends Comparable> Predicate buildDateCondition(
        CriteriaBuilder builder, UIViewCondition condition,
        Class<T> target, Path<?> path,
        BiFunction<Expression<T>, Expression<T>, Predicate> mapper
    ) {
        Object value = getValue(condition.getValue());
        Expression<T> conditionExp = builder.function(
            "TO_DATE", target, builder.literal(value), builder.literal(DB_TO_API_DATE_SHORT_FORMAT)
        );
        return mapper.apply((Expression<T>) path, conditionExp);
    }

    private Expression<Boolean> buildPathValuePredicate(
        CriteriaBuilder builder, Path<?> path, UIViewFetchFieldValuesRequest request
    ) {
        if (request.getSearchText() != null && !request.getSearchText().isBlank()) {
            if (isDateOrTime(path.getJavaType()) || isDayOrWeek(path.getJavaType())) {
                return builder.like(
                    convertDateFieldToString(path.getJavaType(), builder, path),
                    "%" + request.getSearchText().toLowerCase() + "%"
                );
            } else {
                return builder.like(
                    builder.lower(path.as(String.class)), "%" + request.getSearchText().toLowerCase() + "%"
                );
            }
        }

        return builder.conjunction();
    }

    private static <T> Path<T> buildPath(Root<?> root, UIViewCondition condition) {
        Path<T> path = getPath(root, condition.getField());
        JsonNode node = condition.getValue();
        if (node != null && node.isObject() && node.has(ID)) {
            path = path.get(ID);
        }
        return path;
    }

    private static Comparable getValue(JsonNode node) {
        if (node.isNumber()) {
            return node.asLong();
        } else if (node.isObject() && node.has(ID)) {
            return node.get(ID).asLong();
        } else {
            return node.asText();
        }
    }
}
