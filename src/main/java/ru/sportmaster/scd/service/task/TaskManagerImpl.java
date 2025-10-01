package ru.sportmaster.scd.service.task;

import static java.util.Objects.nonNull;
import static ru.sportmaster.scd.utils.EnvironmentUtils.getEnvironmentValues;
import static ru.sportmaster.scd.utils.EnvironmentUtils.restoreEnvironment;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.sportmaster.scd.service.algorithms.AlgorithmExecuteService;
import ru.sportmaster.scd.task.AlgorithmTask;
import ru.sportmaster.scd.task.HazelCastComponent;
import ru.sportmaster.scd.task.QueueTasks;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskManagerImpl implements TaskManager {
    private final ThreadManager threadManager;
    private final QueueTasks queueTasks;
    private final AlgorithmExecuteService algorithmExecuteService;
    private final Map<AlgorithmTask, Future<AlgorithmTask>> startedTasks = new HashMap<>();
    private final HazelCastComponent hazelCastComponent;

    @Getter
    private final Runnable scheduleGetAndStartTasks =
        () ->
            Executors
                .newSingleThreadScheduledExecutor()
                .schedule(
                    this::getAndStartTasks,
                    60,
                    TimeUnit.SECONDS);

    @Override
    public void init() {
        hazelCastComponent.setTaskManager(this);
        if (hazelCastComponent.isFirstClusterMember()) {
            log.debug("First cluster member.");
            Executors
                .newSingleThreadScheduledExecutor()
                .schedule(
                    this::startFirstClusterMember,
                    10,
                    TimeUnit.SECONDS);
        } else {
            log.debug("Non first cluster member.");
            scheduleGetAndStartTasks.run();
        }
    }

    @Override
    public void preDestroy() {
        startedTasks.keySet().forEach(queueTasks::reQueuedTask);
    }

    private void startFirstClusterMember() {
        if (hazelCastComponent.nonMergeCluster()) {
            log.debug("Clear hung tasks.");
            queueTasks.getHungTasks().forEach(queueTasks::reQueuedTask);
        }
        log.debug("Get and start tasks.");
        getAndStartTasks();
    }

    @Override
    public void getAndStartTasks() {
        getAndStartTasksWithEnvironment(getEnvironmentValues());
    }

    private void getAndStartTasksWithEnvironment(Map<String, String> environment) {
        threadManager.oneThreadSubmit(() -> getAndStartTasksExecutor(environment));
    }

    private void getAndStartTasksExecutor(Map<String, String> environment) {
        log.debug("Start tasks executor.");
        restoreEnvironment(environment);
        AlgorithmTask task;
        do {
            task = null;
            if (threadManager.isPoolFreeThread()) {
                try {
                    hazelCastComponent.getQueueLock().lock();
                    task = queueTasks.getTask();
                    if (nonNull(task)) {
                        log.debug("Get task {}.", task);
                        var future = startAlgorithmExecute(task, environment);
                        startedTasks.put(task, future);
                        queueTasks.startTask(task);
                    }
                } finally {
                    hazelCastComponent.getQueueLock().unlock();
                }
            }
        } while (nonNull(task));
        log.debug("Done tasks executor.");
    }

    private Future<AlgorithmTask> startAlgorithmExecute(AlgorithmTask task,
                                                        Map<String, String> environment) {
        return threadManager.poolSubmit(() -> algorithmTaskExecutor(task, environment));
    }

    private AlgorithmTask algorithmTaskExecutor(AlgorithmTask task,
                                                Map<String, String> environment) {
        restoreEnvironment(environment);
        algorithmExecuteService.executeAlgorithm(task, this::doneTask);
        return task;
    }

    private void doneTask(AlgorithmTask task) {
        threadManager.singleSubmit(() -> doneTaskExecutor(task, getEnvironmentValues()));
    }

    private void doneTaskExecutor(AlgorithmTask task,
                                  Map<String, String> environment) {
        restoreEnvironment(environment);
        var future = startedTasks.get(task);
        try {
            future.get();
        } catch (InterruptedException e) {
            log.error("Done task get error {}", e.getMessage(), e);
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            log.error("Done task get error {}", e.getMessage(), e);
        }
        startedTasks.remove(task);
        queueTasks.finishTask(task);
        getAndStartTasksWithEnvironment(environment);
    }
}
