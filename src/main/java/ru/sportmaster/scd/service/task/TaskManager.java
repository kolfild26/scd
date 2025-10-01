package ru.sportmaster.scd.service.task;

public interface TaskManager {
    void init();

    void getAndStartTasks();

    Runnable getScheduleGetAndStartTasks();

    void preDestroy();
}
