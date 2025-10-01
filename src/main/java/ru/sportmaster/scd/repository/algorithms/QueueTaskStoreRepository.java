package ru.sportmaster.scd.repository.algorithms;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.springframework.lang.NonNull;
import ru.sportmaster.scd.entity.algorithms.QueueTaskStore;
import ru.sportmaster.scd.repository.AbstractRepository;
import ru.sportmaster.scd.task.AlgorithmTask;
import ru.sportmaster.scd.task.AlgorithmTaskState;

public interface QueueTaskStoreRepository extends AbstractRepository<QueueTaskStore, Long> {
    QueueTaskStore create(@NonNull AlgorithmTask task,
                          @NonNull AlgorithmTaskState state);

    AlgorithmTaskState load(AlgorithmTask task);

    Map<AlgorithmTask, AlgorithmTaskState> loadAll(Collection<AlgorithmTask> keys);

    Set<AlgorithmTask> loadAllKeys();
}
