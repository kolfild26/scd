package ru.sportmaster.scd.service.caches;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static ru.sportmaster.scd.utils.HistoryUtils.getCurrentRevision;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.sportmaster.scd.task.HazelCastComponent;

@Service
@RequiredArgsConstructor
public class HistoryCacheService {
    private final HazelCastComponent hazelCastComponent;

    @PersistenceContext
    protected EntityManager em;

    public <T> T getEntityVersion(@NonNull Class<T> entityClass,
                                  @NonNull Object entityId,
                                  Long version) {
        var auditReader = AuditReaderFactory.get(em);
        var entityVersion =
            Optional.ofNullable(version)
                .orElse(getCurrentRevision(auditReader, entityClass, entityId));
        var cacheKey =
            HistoryCacheKey.builder()
                .entityClass(entityClass)
                .entityId(entityId)
                .version(entityVersion)
                .build();
        var result = hazelCastComponent.getHistoryCache().get(cacheKey);
        if (isNull(result)) {
            result =
                auditReader.find(
                    entityClass,
                    entityId,
                    entityVersion);
            if (nonNull(result)) {
                hazelCastComponent.getHistoryCache().put(cacheKey, result);
            } else {
                return null;
            }
        }
        return entityClass.cast(result);
    }
}
