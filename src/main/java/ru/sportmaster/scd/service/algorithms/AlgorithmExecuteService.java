package ru.sportmaster.scd.service.algorithms;

import java.util.function.Consumer;
import org.springframework.lang.NonNull;
import ru.sportmaster.scd.task.AlgorithmTask;

public interface AlgorithmExecuteService {
    void executeAlgorithm(@NonNull AlgorithmTask task,
                          @NonNull Consumer<AlgorithmTask> doneFunction);
}
