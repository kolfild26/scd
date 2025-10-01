package ru.sportmaster.scd.exceptions;

import org.springframework.lang.NonNull;
import ru.sportmaster.scd.task.AlgorithmTask;

public class QueueStateException extends RuntimeException {
    private final String algorithmTaskDescription;

    public QueueStateException(@NonNull AlgorithmTask algorithmTask) {
        this.algorithmTaskDescription = algorithmTask.toString();
    }

    @Override
    public String getMessage() {
        return String.format("State Definition error for %s.", algorithmTaskDescription);
    }
}
