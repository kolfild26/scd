package ru.sportmaster.scd.service.task;

import static java.util.Objects.nonNull;

import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sportmaster.scd.task.AlgorithmTask;
import ru.sportmaster.scd.task.AlgorithmTaskState;
import ru.sportmaster.scd.task.HazelCastComponent;

@Service
@RequiredArgsConstructor
public class CheckCancelTaskServiceImpl implements CheckCancelTaskService {
    private final HazelCastComponent hazelCastComponent;

    @Override
    public boolean isTaskCanceled(Map<String, Object> params) {
        var algorithmTask = AlgorithmTask.createFromParams(params);
        if (nonNull(algorithmTask) && nonNull(algorithmTask.getAlgorithmId())) {
            var state = hazelCastComponent.getQueueTasks().get(algorithmTask);
            var canceled = Optional.ofNullable(state)
                .map(AlgorithmTaskState::getCanceled)
                .orElse(null);
            return nonNull(canceled);
        }
        return false;
    }
}
