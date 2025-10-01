package ru.sportmaster.scd.repository.mementity;

import static java.util.Objects.isNull;

import jakarta.persistence.criteria.Predicate;
import java.util.Optional;
import org.springframework.lang.NonNull;
import ru.sportmaster.scd.entity.mementity.FilterSystemLog;
import ru.sportmaster.scd.repository.AbstractRepositoryImpl;

public class AbstractFilterSystemRepositoryImpl<T, K>
    extends AbstractRepositoryImpl<T, K>
    implements AbstractFilterSystemRepository<T, K> {
    private final FilterSystemLogRepository fsLogRepository;

    public AbstractFilterSystemRepositoryImpl(Class<T> clazz,
                                              FilterSystemLogRepository fsLogRepository) {
        super(clazz);
        this.fsLogRepository = fsLogRepository;
    }

    @Override
    public Predicate getFilterPredicate(@NonNull Predicate predicate,
                                        @NonNull FilterSystemPredicateBuilder predicateBuilder) {
        var typeSelection = getTypeSelection(predicateBuilder);
        if (isNull(typeSelection)) {
            return predicate;
        }
        return
            predicateBuilder.getFilterPredicate(predicate, typeSelection);
    }


    private Byte getTypeSelection(@NonNull FilterSystemPredicateBuilder predicateBuilder) {
        if (predicateBuilder.nonValid()) {
            return null;
        }
        return
            Optional.ofNullable(fsLogRepository.findById(predicateBuilder.getFilterSystemLogKey()))
                .map(FilterSystemLog::getTypeSelection)
                .orElse(null);
    }
}
