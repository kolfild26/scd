package ru.sportmaster.scd.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.lang.NonNull;

/**
 * Служебный класс, предоставляющий общий функционал работы с версиями описания алгоритмов.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HistoryUtils {
    /**
     * Получение номера текущей версии описания по идентификатору.
     * @param reader - сервис обслуживания версий
     * @param clazz - класс описания алгоритма
     * @param id - идентификатор описания
     * @return - номер версии
     */
    public static Long getCurrentRevision(@NonNull AuditReader reader,
                                          @NonNull Class<?> clazz,
                                          @NonNull Object id) {
        return (Long) reader.createQuery()
            .forRevisionsOfEntity(clazz, true, true)
            .addProjection(AuditEntity.revisionNumber().max())
            .add(AuditEntity.id().eq(id))
            .getSingleResult();
    }
}
