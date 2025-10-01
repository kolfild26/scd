package ru.sportmaster.scd.utils;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static ru.sportmaster.scd.consts.ParamNames.NAME;
import static ru.sportmaster.scd.utils.UiUtil.DB_TO_API_DATETIME_FORMAT;
import static ru.sportmaster.scd.utils.UiUtil.DB_TO_API_DATE_FORMAT;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
import ru.sportmaster.scd.entity.dictionary.Day;
import ru.sportmaster.scd.entity.dictionary.Week;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JpaUtil {
    public static final String DELIMITER_REG_EXP = "[.]";

    public static final Long MAX_ORDER = 99999L;

    public static final String HIBERNATE_TAG = "$$_hibernate";

    public static <Y> Path<Y> getPath(@NonNull Root<?> root,
                                      @NonNull String fieldName) {
        Path<Y> result = null;
        for (String s : fieldName.split(DELIMITER_REG_EXP)) {
            if (isNull(result)) {
                result = root.get(s.trim());
            } else {
                result = result.get(s.trim());
            }
        }
        return result;
    }

    public static void buildJoinsOrFetches(Root<?> root, String fieldName, boolean distinct) {
        Fetch<?, ?> fetch = null;
        Join<?, ?> join = null;
        String[] path = fieldName.split(DELIMITER_REG_EXP);
        if (path.length > 1) {
            for (int i = 0; i < path.length - 1; i++) {
                String field = path[i];
                if (i == 0) {
                    if (distinct) {
                        fetch = root.fetch(field);
                    } else {
                        join = root.join(field);
                    }
                } else {
                    if (distinct) {
                        fetch = fetch.fetch(field);
                    } else {
                        join = join.join(field);
                    }
                }
            }
        }
    }

    public static Class<?> getOriginalClass(Object proxy) {
        return proxy instanceof HibernateProxy ? proxy.getClass().getSuperclass() : proxy.getClass();
    }

    @SneakyThrows
    public static Object getValue(Object obj, String path) {
        Object result = obj;
        for (String field : path.split(DELIMITER_REG_EXP)) {
            if (isNull(result)) {
                return null;
            }

            Class<?> fieldClazz = getOriginalClass(result);
            Method method = fieldClazz.getMethod("get" + StringUtils.capitalize(field));
            result = method.invoke(result);
        }
        return result;
    }

    public static Expression<String> convertDateFieldToString(Class<?> type, CriteriaBuilder builder, Path<?> path) {
        if (LocalDateTime.class == type || type.isAssignableFrom(Timestamp.class)) {
            return builder.function(
                "TO_CHAR", String.class, path, builder.literal(DB_TO_API_DATETIME_FORMAT)
            );
        } else if (Week.class == type || Day.class == type) {
            return builder.function(
                "TO_CHAR", String.class, path.get(NAME), builder.literal(DB_TO_API_DATE_FORMAT)
            );
        } else {
            return builder.function(
                "TO_CHAR", String.class, path, builder.literal(DB_TO_API_DATE_FORMAT)
            );
        }
    }

    public static String getTableName(Class<?> type) {
        return
            Optional.ofNullable(type.getAnnotation(Table.class))
                .map(Table::name)
                .orElse(null);
    }

    public static String getFullTableName(Class<?> type) {
        return
            Optional.ofNullable(type.getAnnotation(Table.class))
                .map(table -> {
                        var tableSchema = table.schema();
                        if (nonNull(tableSchema) && !tableSchema.isEmpty()) {
                            return tableSchema + "." + table.name();
                        }
                        return table.name();
                    }
                )
                .orElse(null);
    }

    public static String getIdFieldTableName(Class<?> type) {
        for (var field: type.getDeclaredFields()) {
            if (isIdField(field)) {
                return
                    Optional.ofNullable(field.getAnnotation(Column.class))
                        .map(Column::name)
                        .orElse(null);
            }
        }
        return null;
    }

    private static boolean isIdField(Field field) {
        return nonNull(field.getAnnotation(Id.class));
    }

    public static boolean isEmbeddable(Class<?> clazz) {
        return clazz.isAnnotationPresent(Embeddable.class);
    }
}
