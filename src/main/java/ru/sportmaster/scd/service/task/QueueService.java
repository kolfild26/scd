package ru.sportmaster.scd.service.task;

import java.util.List;
import org.springframework.lang.NonNull;
import ru.sportmaster.scd.dto.AlgorithmQueueDto;

public interface QueueService {
    List<AlgorithmQueueDto> getQueueState();

    int cancelAlgorithm(@NonNull Long algorithmId);

    int cancelGroup(@NonNull Long algorithmGroup);
}
