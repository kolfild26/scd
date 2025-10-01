package ru.sportmaster.scd.service.task;

import java.util.Map;

public interface CheckCancelTaskService {
    boolean isTaskCanceled(Map<String, Object> params);
}
