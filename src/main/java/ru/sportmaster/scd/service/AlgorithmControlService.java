package ru.sportmaster.scd.service;

import org.springframework.lang.NonNull;
import ru.sportmaster.scd.algorithms.AlgComponentStatus;
import ru.sportmaster.scd.algorithms.information.AlgorithmInfo;
import ru.sportmaster.scd.dto.AlgorithmTasksDto;
import ru.sportmaster.scd.dto.PutAlgorithmsResponseDto;
import ru.sportmaster.scd.web.UserEnvironment;

public interface AlgorithmControlService {
    PutAlgorithmsResponseDto putAlgorithms(@NonNull AlgorithmTasksDto algorithmTasks,
                                           UserEnvironment environment);

    AlgComponentStatus getAlgorithmStatus(@NonNull Long algorithmId);

    AlgComponentStatus getAlgStageStatus(@NonNull Long algStageId);

    AlgComponentStatus getAlgStepStatus(@NonNull Long algStepId);

    AlgorithmInfo getAlgorithmInfo(@NonNull Long algorithmId);

    boolean serviceStartExecuteAlgorithms();
}
