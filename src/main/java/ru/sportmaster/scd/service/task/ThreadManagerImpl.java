package ru.sportmaster.scd.service.task;

import static java.util.Objects.isNull;

import jakarta.annotation.PreDestroy;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.sportmaster.scd.task.AlgorithmTask;

@Service
public class ThreadManagerImpl implements ThreadManager {
    private final ThreadPoolExecutor poolExecutor;
    private final int poolNumThreads;
    private final ExecutorService singleExecutor;
    private final ExecutorService oneExecutor;
    private Future<?> oneFuture;

    public ThreadManagerImpl(@Value("${scd.task.pool-num-thread:4}") int poolNumThreads) {
        this.poolNumThreads = poolNumThreads;
        this.poolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(this.poolNumThreads);
        this.singleExecutor = Executors.newCachedThreadPool();
        this.oneExecutor = Executors.newSingleThreadExecutor();
    }

    @PreDestroy
    private void preDestroy() {
        poolExecutor.shutdown();
        singleExecutor.shutdown();
        oneExecutor.shutdown();
        shutdownExecutor(poolExecutor);
        shutdownExecutor(singleExecutor);
        shutdownExecutor(oneExecutor);
    }

    private void shutdownExecutor(ExecutorService executorService) {
        try {
            if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public boolean isPoolFreeThread() {
        return poolExecutor.getActiveCount() < poolNumThreads;
    }

    @Override
    public Future<AlgorithmTask> poolSubmit(Callable<AlgorithmTask> task) {
        return poolExecutor.submit(task);
    }

    @Override
    public void singleSubmit(Runnable task) {
        singleExecutor.submit(task);
    }

    @Override
    public synchronized void oneThreadSubmit(Runnable task) {
        if (isNull(oneFuture) || oneFuture.isDone()) {
            oneFuture = oneExecutor.submit(task);
        }
    }
}
