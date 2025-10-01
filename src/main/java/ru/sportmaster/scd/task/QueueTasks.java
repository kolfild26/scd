package ru.sportmaster.scd.task;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.lang.NonNull;

public interface QueueTasks {
    void addTask(@NonNull AlgorithmTask algorithmTask);

    void startTask(@NonNull AlgorithmTask algorithmTask);

    void finishTask(@NonNull AlgorithmTask algorithmTask);

    void setAbnormalDone(@NonNull AlgorithmTask algorithmTask);

    void reQueuedTask(@NonNull AlgorithmTask algorithmTask);

    void cancelGroup(@NonNull AlgorithmTask algorithmTask);

    int cancelGroup(@NonNull Long algorithmGroup);

    AlgorithmTask getTask();

    List<Map.Entry<AlgorithmTask, AlgorithmTaskState>> getQueueState();

    int cancelAlgorithm(@NonNull Long algorithmId);

    Long getGroupId();

    Collection<AlgorithmTask> getHungTasks();
}
