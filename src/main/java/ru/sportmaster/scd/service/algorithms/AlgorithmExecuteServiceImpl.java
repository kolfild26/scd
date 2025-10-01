package ru.sportmaster.scd.service.algorithms;

import static java.util.Objects.nonNull;
import static ru.sportmaster.scd.consts.ParamNames.ERROR_STEP_PARAM_NAME;

import java.util.HashMap;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.sportmaster.scd.task.AlgorithmTask;
import ru.sportmaster.scd.task.QueueTasks;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlgorithmExecuteServiceImpl implements AlgorithmExecuteService {
    private final AlgorithmLockService algorithmLockService;
    private final AlgorithmService algorithmService;
    private final QueueTasks queueTasks;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void executeAlgorithm(@NonNull AlgorithmTask task,
                                 @NonNull Consumer<AlgorithmTask> doneFunction) {
        log.debug("Executing algorithm service started.");
        try {
            if (algorithmLockService.lock(task.getPartitionId(), task.getPartitionType())) {
                var params = new HashMap<String, Object>();
                algorithmService.execute(task.getAlgorithmId(), params);
                if (nonNull(params.get(ERROR_STEP_PARAM_NAME))
                    && task.isStopOnError()) {
                    queueTasks.cancelGroup(task);
                }
            } else {
                queueTasks.setAbnormalDone(task);
                log.debug("Database blocked. Returning to queue.");
            }
        } finally {
            log.debug("Executing algorithm service done.");
            doneFunction.accept(task);
            algorithmLockService.unLock(task.getPartitionId(), task.getPartitionType());
        }
    }
}
