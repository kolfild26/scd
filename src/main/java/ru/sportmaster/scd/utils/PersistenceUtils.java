package ru.sportmaster.scd.utils;

import static java.util.Objects.isNull;

import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.lang.NonNull;
import ru.sportmaster.scd.entity.IEntity;

/**
 * Служебный класс, предоставляющий общий функционал работы с сущностями БД.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PersistenceUtils {
    public static int hashCode(@NonNull IEntity<Long> value) {
        return isNull(value.getId()) ? 0 : Long.hashCode(value.getId());
    }

    public static boolean equals(@NonNull IEntity<?> source,
                                 Object o) {
        if (source == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(source) != Hibernate.getClass(o)) {
            return false;
        }
        return source.getId() != null && Objects.equals(source.getId(), ((IEntity<?>) o).getId());
    }
}
