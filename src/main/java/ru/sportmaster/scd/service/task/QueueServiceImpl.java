package ru.sportmaster.scd.service.task;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.sportmaster.scd.dto.AlgorithmQueueDto;
import ru.sportmaster.scd.task.QueueTasks;

@Service
@RequiredArgsConstructor
public class QueueServiceImpl implements QueueService {
    private final QueueTasks queueTasks;

    @Override
    public List<AlgorithmQueueDto> getQueueState() {
        return queueTasks.getQueueState().stream()
            .map(entry -> AlgorithmQueueDto.create(entry.getKey(), entry.getValue()))
            .toList();
    }

    @Override
    public int cancelAlgorithm(@NonNull Long algorithmId) {
        return queueTasks.cancelAlgorithm(algorithmId);
    }

    @Override
    public int cancelGroup(@NonNull Long algorithmGroup) {
        return queueTasks.cancelGroup(algorithmGroup);
    }
}
