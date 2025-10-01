package ru.sportmaster.scd.repository.mementity;

import org.springframework.stereotype.Repository;
import ru.sportmaster.scd.entity.mementity.FilterSystemLog;
import ru.sportmaster.scd.entity.mementity.FilterSystemLogKey;
import ru.sportmaster.scd.repository.AbstractRepositoryImpl;

@Repository
public class FilterSystemLogRepositoryImpl
    extends AbstractRepositoryImpl<FilterSystemLog, FilterSystemLogKey>
    implements FilterSystemLogRepository {
    public FilterSystemLogRepositoryImpl() {
        super(FilterSystemLog.class);
    }
}
