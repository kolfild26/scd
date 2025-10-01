package ru.sportmaster.scd.service.task;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import ru.sportmaster.scd.task.AlgorithmTask;

public interface ThreadManager {
    boolean isPoolFreeThread();

    Future<AlgorithmTask> poolSubmit(Callable<AlgorithmTask> task);

    void singleSubmit(Runnable task);

    void oneThreadSubmit(Runnable task);
}
