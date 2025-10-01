package ru.sportmaster.scd.repository.algorithms;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.util.Pair;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.sportmaster.scd.entity.algorithms.QueueTaskStore;
import ru.sportmaster.scd.repository.AbstractRepositoryImpl;
import ru.sportmaster.scd.task.AlgorithmTask;
import ru.sportmaster.scd.task.AlgorithmTaskState;

@Repository
public class QueueTaskStoreRepositoryImpl
    extends AbstractRepositoryImpl<QueueTaskStore, Long>
    implements QueueTaskStoreRepository {

    public QueueTaskStoreRepositoryImpl() {
        super(QueueTaskStore.class);
    }

    @Override
    public QueueTaskStore create(@NonNull AlgorithmTask task,
                                 @NonNull AlgorithmTaskState state) {
        return QueueTaskStore.builder()
            .algorithmId(task.getAlgorithmId())
            .partitionType(task.getPartitionType())
            .partitionId(task.getPartitionId())
            .groupAlgorithm(task.getGroupAlgorithm())
            .calcId(task.getCalcId())
            .isStopOnError(task.isStopOnError() ? true : null)
            .taskState(state.toBuilder().build())
            .build();
    }

    @Override
    public AlgorithmTaskState load(AlgorithmTask task) {
        if (nonNull(task)) {
            return Optional.ofNullable(findById(task.getAlgorithmId()))
                .map(QueueTaskStore::getTaskState)
                .orElse(null);
        }
        return null;
    }

    @Override
    public Map<AlgorithmTask, AlgorithmTaskState> loadAll(Collection<AlgorithmTask> keys) {
        if (nonNull(keys) && !keys.isEmpty()) {
            return keys.stream()
                .filter(Objects::nonNull)
                .map(this::loadState)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
        }
        return Collections.emptyMap();
    }

    private Pair<AlgorithmTask, AlgorithmTaskState> loadState(@NonNull AlgorithmTask task) {
        var entry = findById(task.getAlgorithmId());
        if (isNull(entry)) {
            return null;
        }
        return Pair.of(task, entry.getTaskState());
    }

    @Override
    public Set<AlgorithmTask> loadAllKeys() {
        return findAll().stream().map(this::getKey).collect(Collectors.toSet());
    }

    private AlgorithmTask getKey(@NonNull QueueTaskStore entry) {
        return AlgorithmTask.builder()
            .algorithmId(entry.getAlgorithmId())
            .partitionType(entry.getPartitionType())
            .partitionId(entry.getPartitionId())
            .groupAlgorithm(entry.getGroupAlgorithm())
            .calcId(entry.getCalcId())
            .isStopOnError(Optional.ofNullable(entry.getIsStopOnError()).orElse(false))
            .build();
    }
}
