package ru.sportmaster.scd.task;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.exceptions.QueueStateException;
import ru.sportmaster.scd.repository.algorithms.execution.AlgorithmRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class QueueTasksImpl implements QueueTasks {
    private final HazelCastComponent hazelCastComponent;
    private final AlgorithmRepository algorithmRepository;
    private final IPartitionService partitionService;

    @Override
    public void addTask(@NonNull AlgorithmTask algorithmTask) {
        hazelCastComponent.getQueueTasks().put(
            algorithmTask,
            AlgorithmTaskState.builder()
                .status(AlgorithmTaskStatus.QUEUED)
                .created(LocalDateTime.now())
                .order(hazelCastComponent.getIdGenerator().newId())
                .build()
        );
        log.debug("Алгоритм {} добавлен в очередь.", algorithmTask);
    }

    @Override
    public void startTask(@NonNull AlgorithmTask algorithmTask) {
        var state = getState(algorithmTask);
        state.setStatus(AlgorithmTaskStatus.EXECUTING);
        state.setStarted(LocalDateTime.now());
        hazelCastComponent.getQueueTasks().replace(algorithmTask, state);
        log.debug("Задание {} запущено.", algorithmTask);
    }

    @Override
    public void finishTask(@NonNull AlgorithmTask algorithmTask) {
        var state = getState(algorithmTask);
        if (state.isNormalDone()) {
            hazelCastComponent.getQueueTasks().remove(algorithmTask);
        } else {
            state.setNormalDone(true);
            state.setStatus(AlgorithmTaskStatus.QUEUED);
            hazelCastComponent.getQueueTasks().replace(algorithmTask, state);
        }
        log.debug("Задание {} закончено.", algorithmTask);
    }

    @NonNull
    private AlgorithmTaskState getState(@NonNull AlgorithmTask algorithmTask) {
        return
            Optional.ofNullable(hazelCastComponent.getQueueTasks().get(algorithmTask))
                .orElseThrow(() -> new QueueStateException(algorithmTask));
    }

    @Override
    public void setAbnormalDone(@NonNull AlgorithmTask algorithmTask) {
        var state = getState(algorithmTask);
        state.setNormalDone(false);
        hazelCastComponent.getQueueTasks().replace(algorithmTask, state);
        log.debug("Заданию {} установлен флаг ненормального окончания.", algorithmTask);
    }

    @Override
    public void reQueuedTask(@NonNull AlgorithmTask algorithmTask) {
        var state = getState(algorithmTask);
        state.setStatus(AlgorithmTaskStatus.QUEUED);
        hazelCastComponent.getQueueTasks().replace(algorithmTask, state);
        log.debug("Задание {} готово к перезапуску.", algorithmTask);
    }

    @Override
    public void cancelGroup(@NonNull AlgorithmTask algorithmTask) {
        cancelTasks(
            hazelCastComponent.getQueueTasks().entrySet()
                .stream()
                .filter(entry ->
                    algorithmTask.getGroupAlgorithm().equals(entry.getKey().getGroupAlgorithm())
                        && isNull(entry.getValue().getCanceled()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet())
        );
        log.debug("Задание {} на отмену группы выполнено.", algorithmTask);
    }

    @Override
    public int cancelGroup(@NonNull Long algorithmGroup) {
        return cancelAlgorithms(entry -> algorithmGroup.equals(entry.getKey().getGroupAlgorithm()));
    }

    private int cancelAlgorithms(@NonNull Predicate<Map.Entry<AlgorithmTask, AlgorithmTaskState>> predicate) {
        var algorithmToCancel = hazelCastComponent.getQueueTasks().entrySet()
            .stream()
            .filter(entry -> predicate.test(entry) && isNull(entry.getValue().getCanceled()))
            .map(Map.Entry::getKey)
            .collect(Collectors.toSet());
        cancelTasks(algorithmToCancel);
        return algorithmToCancel.size();
    }

    private void cancelTasks(Collection<AlgorithmTask> tasks) {
        if (isNull(tasks) || tasks.isEmpty()) {
            return;
        }
        var cancelTime = LocalDateTime.now();
        for (var queueTask : tasks) {
            var algorithmId = queueTask.getAlgorithmId();
            try {
                algorithmRepository.cancel(algorithmId, cancelTime);
            } catch (Exception exception) {
                log.error("Set cancel algorithm {} error {}", algorithmId, exception.getMessage(), exception);
            }
            var state = getState(queueTask);
            state.setCanceled(cancelTime);
            hazelCastComponent.getQueueTasks().replace(queueTask, state);
            log.debug("Задание {} отменено.", queueTask);
        }
    }


    @Override
    public AlgorithmTask getTask() {
        var queueItem = hazelCastComponent.getQueueTasks()
            .entrySet()
            .stream()
            .filter(entry -> filterStatus(entry) && filterBlocking(entry))
            .min(this::sortQueue)
            .orElse(null);
        if (nonNull(queueItem)) {
            Optional.ofNullable(queueItem.getValue())
                .ifPresent(value -> value.setStatus(AlgorithmTaskStatus.PEEKED));
            log.debug("Задание {} получено из очереди.", queueItem.getKey());
            return queueItem.getKey();
        }
        return null;
    }

    private boolean filterStatus(Map.Entry<AlgorithmTask, AlgorithmTaskState> entry) {
        return
            AlgorithmTaskStatus.QUEUED.equals(
                Optional.ofNullable(entry)
                    .map(Map.Entry::getValue)
                    .map(AlgorithmTaskState::getStatus)
                    .orElse(null)
            );
    }

    private boolean filterBlocking(Map.Entry<AlgorithmTask, AlgorithmTaskState> entry) {
        var blocking = getBlocking();
        if (isNull(entry) || isNull(blocking) || blocking.isEmpty()) {
            return true;
        }
        var partitions =
            entry.getKey()
                .getPartitionType()
                .getPartitionDivTmaIds(
                    entry.getKey().getPartitionId(),
                    partitionService
                );
        return blocking.stream().allMatch(item -> item.nonBlocking(partitions));
    }

    private List<PartitionItemBlock> getBlocking() {
        return hazelCastComponent.getQueueTasks().entrySet()
            .stream()
            .filter(entry -> !AlgorithmTaskStatus.QUEUED.equals(entry.getValue().getStatus()))
            .map(Map.Entry::getKey)
            .map(algorithmTask ->
                algorithmTask.getPartitionType()
                    .getPartitionDivTmaIds(
                        algorithmTask.getPartitionId(),
                        partitionService
                    )
            )
            .map(PartitionItemBlock::create)
            .distinct()
            .toList();
    }

    private int sortQueue(Map.Entry<AlgorithmTask, AlgorithmTaskState> entry1,
                          Map.Entry<AlgorithmTask, AlgorithmTaskState> entry2) {
        var order1 = getOrder(entry1);
        var order2 = getOrder(entry2);
        if (isNull(order1) && isNull(order2)) {
            return 0;
        }
        if (isNull(order1)) {
            return 1;
        }
        if (isNull(order2)) {
            return 0;
        }
        return Long.compare(order1, order2);
    }

    private Long getOrder(Map.Entry<AlgorithmTask, AlgorithmTaskState> entry) {
        return Optional.ofNullable(entry)
            .map(Map.Entry::getValue)
            .map(AlgorithmTaskState::getOrder)
            .orElse(null);
    }

    @Override
    public List<Map.Entry<AlgorithmTask, AlgorithmTaskState>> getQueueState() {
        return hazelCastComponent.getQueueTasks()
            .entrySet()
            .stream()
            .sorted(this::sortQueue)
            .toList();
    }

    @Override
    public int cancelAlgorithm(@NonNull Long algorithmId) {
        return cancelAlgorithms(entry -> algorithmId.equals(entry.getKey().getAlgorithmId()));
    }

    @Override
    public Long getGroupId() {
        return hazelCastComponent.getIdGenerator().newId();
    }

    @Override
    public Collection<AlgorithmTask> getHungTasks() {
        return hazelCastComponent.getQueueTasks().entrySet()
            .stream()
            .filter(entry -> AlgorithmTaskStatus.EXECUTING.equals(entry.getValue().getStatus()))
            .map(Map.Entry::getKey)
            .collect(Collectors.toSet());
    }
}
