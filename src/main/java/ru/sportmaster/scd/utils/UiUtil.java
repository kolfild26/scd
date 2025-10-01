package ru.sportmaster.scd.utils;

import static java.util.Optional.ofNullable;
import static ru.sportmaster.scd.consts.ParamNames.ID;
import static ru.sportmaster.scd.utils.JpaUtil.getOriginalClass;

import com.fasterxml.jackson.databind.JsonNode;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.hibernate.internal.util.ReflectHelper;
import org.springframework.lang.NonNull;
import org.springframework.util.ReflectionUtils;
import ru.sportmaster.scd.entity.dictionary.Day;
import ru.sportmaster.scd.entity.dictionary.Week;
import ru.sportmaster.scd.ui.view.type.ICondition;
import ru.sportmaster.scd.ui.view.type.UIViewCondition;
import ru.sportmaster.scd.ui.view.type.UIViewGroupCondition;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UiUtil {
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    public static final DateTimeFormatter DB_CALENDAR_ID_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static final String DB_TO_API_DATETIME_FORMAT = "DD.MM.YYYY HH24:MI:SS";

    public static final String DB_TO_API_DATE_FORMAT = "DD.MM.YYYY";

    public static final String DB_TO_API_DATE_SHORT_FORMAT = "DD.MM.YY";

    public static boolean isDateOrTime(Class<?> clazz) {
        return isDate(clazz) || LocalDateTime.class == clazz;
    }

    public static boolean isDayOrWeek(Class<?> clazz) {
        return Day.class == clazz || Week.class == clazz;
    }

    public static boolean isDate(Class<?> clazz) {
        return LocalDate.class == clazz || Date.class == clazz;
    }

    public static boolean isDate(Object entity) {
        return isDate(entity.getClass());
    }

    public static Long findEntityId(List<ICondition> conditions, String entityPath) {
        return findEntityConditionValue(conditions, entityPath, UiUtil::getId);
    }

    public static <T> T findEntityConditionValue(List<ICondition> conditions,
                                                 String entityPath,
                                                 BiFunction<UIViewCondition, String, T> mapper) {
        if (conditions != null && !conditions.isEmpty()) {
            return getEntityConditionValue(conditions, entityPath, mapper);
        }
        return null;
    }

    private static <T> T getEntityConditionValue(@NonNull List<ICondition> conditions,
                                                 String entityPath,
                                                 BiFunction<UIViewCondition, String, T> mapper) {
        T target = null;
        for (ICondition condition : conditions) {
            if (condition.isGroup()) {
                UIViewGroupCondition group = (UIViewGroupCondition) condition;
                T id = findEntityConditionValue(group.getConditions(), entityPath, mapper);
                if (id != null) {
                    return id;
                }
            } else {
                UIViewCondition viewCondition = (UIViewCondition) condition;
                target = mapper.apply(viewCondition, entityPath);
                if (target != null) {
                    break;
                }
            }
        }
        return target;
    }

    private static Long getId(UIViewCondition viewCondition, String entityPath) {
        String field = viewCondition.getField();
        if (field.startsWith(entityPath) && field.endsWith(ID)) {
            return viewCondition.getValue().asLong();
        } else if (field.equalsIgnoreCase(entityPath)) {
            return viewCondition.getValue().get(ID).asLong();
        }
        return null;
    }

    public static Long getId(JsonNode jsonNode, String path) {
        var node = jsonNode.get(path);
        if (node.isNumber()) {
            return node.asLong();
        } else if (node.isObject() && node.has(ID)) {
            return node.get(ID).asLong();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static Comparable<Object> getFieldValue(String path, Object entity) {
        Object obj = entity;
        for (String i : path.split("\\.")) {
            if (obj != null) {
                obj = getObjectValue(obj, i);
            }
        }
        return (Comparable<Object>) obj;
    }

    public static String safeToString(Object value) {
        return ofNullable(value).map(Object::toString).orElse(null);
    }

    private static Object getObjectValue(Object obj, String path) {
        Class<?> clazz = getOriginalClass(obj);
        Field field = ReflectHelper.findField(clazz, path);
        return getValue(field, obj);
    }

    @SneakyThrows
    private static Object getValue(Field field, Object entity) {
        Method getter = ReflectHelper.findGetterMethod(entity.getClass(), field.getName());
        if (Objects.isNull(getter)) {
            return ReflectionUtils.getField(field, entity);
        } else {
            ReflectionUtils.makeAccessible(getter);
            return getter.invoke(entity);
        }
    }
}
