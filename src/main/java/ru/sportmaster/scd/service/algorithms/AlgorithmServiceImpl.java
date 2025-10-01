package ru.sportmaster.scd.service.algorithms;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static ru.sportmaster.scd.consts.ParamNames.ALGORITHM_ID;
import static ru.sportmaster.scd.consts.ParamNames.CURRENT_STAGE_PARAM_NAME;
import static ru.sportmaster.scd.consts.ParamNames.CURRENT_STEP_PARAM_NAME;
import static ru.sportmaster.scd.consts.ParamNames.ERROR_STEP_PARAM_NAME;
import static ru.sportmaster.scd.consts.ParamNames.EXEC_ACTION_PARAM_NAME;
import static ru.sportmaster.scd.consts.ParamNames.NEXT_STAGE_PARAM_NAME;
import static ru.sportmaster.scd.consts.ParamNames.NEXT_STEP_PARAM_NAME;
import static ru.sportmaster.scd.consts.ParamNames.P_ID_STEP;
import static ru.sportmaster.scd.consts.ParamNames.REQUEST_UUID_FIELD;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.sportmaster.scd.algorithms.IExecutor;
import ru.sportmaster.scd.algorithms.executing.AlgorithmExec;
import ru.sportmaster.scd.algorithms.executing.AlgorithmExecutingAction;
import ru.sportmaster.scd.algorithms.executing.AlgorithmStageExec;
import ru.sportmaster.scd.algorithms.executing.AlgorithmStepExec;
import ru.sportmaster.scd.entity.algorithms.definitions.AlgorithmDefinition;
import ru.sportmaster.scd.entity.algorithms.definitions.AlgorithmStageDefinition;
import ru.sportmaster.scd.entity.algorithms.definitions.AlgorithmStepDefinition;
import ru.sportmaster.scd.entity.algorithms.execution.Algorithm;
import ru.sportmaster.scd.exceptions.StepExecuteErrorException;
import ru.sportmaster.scd.repository.algorithms.execution.AlgorithmRepository;
import ru.sportmaster.scd.repository.algorithms.execution.AlgorithmStageRepository;
import ru.sportmaster.scd.repository.algorithms.execution.AlgorithmStepRepository;
import ru.sportmaster.scd.service.airflow.IAirflowService;
import ru.sportmaster.scd.service.caches.HistoryCacheService;
import ru.sportmaster.scd.service.task.CheckCancelTaskService;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlgorithmServiceImpl implements AlgorithmService {
    private final AlgorithmRepository algorithmRepository;
    private final AlgorithmStageRepository algorithmStageRepository;
    private final AlgorithmStepRepository algorithmStepRepository;
    private final HistoryCacheService historyCacheService;
    private final CheckCancelTaskService checkCancelTaskService;
    private final List<IExecutor> executors;
    private final IAirflowService airflowService;

    @Override
    public void execute(@NonNull Long algorithmId,
                        @NonNull Map<String, Object> params) {
        try {
            log.info("Start execute algorithm-{}.", algorithmId);
            var algorithm = algorithmRepository.start(algorithmId);
            params.putAll(algorithm.getParams());
            final var parentRequestUUID = MDC.get(REQUEST_UUID_FIELD);
            final var valueRequestUUID = (String) params.get(REQUEST_UUID_FIELD);
            if (nonNull(valueRequestUUID)) {
                log.info("For algorithm-{} restore requestUUID-{}.", algorithmId, valueRequestUUID);
                MDC.put(REQUEST_UUID_FIELD, valueRequestUUID);
                log.info("Started execute algorithm-{}.", algorithmId);
            }
            params.put(ALGORITHM_ID, algorithm.getId().toString());
            executeAlgorithm(
                getAlgorithmExec(algorithm),
                params
            );
            if (nonNull(valueRequestUUID)) {
                log.info("Finished execute algorithm-{}.", algorithm.getId());
                MDC.put(REQUEST_UUID_FIELD, parentRequestUUID);
            }
            log.info("Finish execute algorithm-{}.", algorithm.getId());
        } catch (Exception exception) {
            log.error("Error execute algorithm-{}.", algorithmId, exception);
            log.info("Finish execute algorithm with error-{}.", algorithmId);
        } finally {
            algorithmRepository.finish(algorithmId, params);
            airflowService.finishAlgorithm(algorithmId, params);
        }
    }

    private AlgorithmExec getAlgorithmExec(@NonNull Algorithm algorithm) {
        return
            new AlgorithmExec(
                algorithm,
                getStageExecs(
                    historyCacheService.getEntityVersion(
                        AlgorithmDefinition.class,
                        algorithm.getAlgorithmDefinition().getId(),
                        algorithm.getDefVersion()
                    )
                )
            );
    }

    private List<AlgorithmStageExec> getStageExecs(@NonNull AlgorithmDefinition definition) {
        return
            definition.getAlgorithmStageDefinitions().stream()
                .filter(AlgorithmStageDefinition::nonDeleted)
                .sorted(Comparator.comparingInt(AlgorithmStageDefinition::getOrder))
                .map(this::createAlgorithmStageExec)
                .toList();
    }

    private AlgorithmStageExec createAlgorithmStageExec(@NonNull AlgorithmStageDefinition stageDefinition) {
        return
            new AlgorithmStageExec(
                stageDefinition,
                getStepExecs(stageDefinition)
            );
    }

    private List<AlgorithmStepExec> getStepExecs(@NonNull AlgorithmStageDefinition stageDefinition) {
        return
            stageDefinition.getAlgorithmStepDefinitions().stream()
                .filter(AlgorithmStepDefinition::nonDeleted)
                .sorted(Comparator.comparingInt(AlgorithmStepDefinition::getOrder))
                .map(this::createAlgorithmStepExec)
                .toList();
    }

    private AlgorithmStepExec createAlgorithmStepExec(@NonNull AlgorithmStepDefinition stepDefinition) {
        return
            new AlgorithmStepExec(
                stepDefinition,
                executors.stream()
                    .filter(executor -> Objects.equals(stepDefinition.getExecutorName(), executor.getName()))
                    .findFirst()
                    .orElseThrow(() ->
                        new IllegalArgumentException(
                            String.format("Executor for %s not found.", stepDefinition)
                        )
                    )
            );
    }

    private void executeAlgorithm(@NonNull AlgorithmExec algorithmExec,
                                  @NonNull Map<String, Object> params) {
        var action = getAction(params);
        var stageExec = getAlgorithmStageExec(algorithmExec, params, action);
        while (nonNull(stageExec) && !AlgorithmExecutingAction.FINISH.equals(action)) {
            log.debug("Execute stage order: {}", stageExec.getStageDefinition().getOrder());
            Throwable throwable = null;
            if (params.containsKey(ERROR_STEP_PARAM_NAME)) {
                throwable = (Throwable) params.get(ERROR_STEP_PARAM_NAME);
                params.remove(ERROR_STEP_PARAM_NAME);
            }
            executeStage(algorithmExec, stageExec, params);
            if (nonNull(throwable)) {
                params.put(ERROR_STEP_PARAM_NAME, throwable);
            }
            action = getAction(params);
            stageExec = getAlgorithmStageExec(algorithmExec, params, action);
            action = getAction(params);
        }
    }

    private AlgorithmExecutingAction getAction(@NonNull Map<String, Object> params) {
        return
            Optional.ofNullable(params.get(EXEC_ACTION_PARAM_NAME))
                .map(AlgorithmExecutingAction.class::cast)
                .orElse(AlgorithmExecutingAction.EXEC_NEXT);
    }

    private void setAction(@NonNull AlgorithmExecutingAction action,
                           @NonNull Map<String, Object> params) {
        params.put(EXEC_ACTION_PARAM_NAME, action);
    }

    private AlgorithmStageExec getAlgorithmStageExec(@NonNull AlgorithmExec algorithmExec,
                                                     @NonNull Map<String, Object> params,
                                                     @NonNull AlgorithmExecutingAction action) {
        if (checkCancelTaskService.isTaskCanceled(params)) {
            log.debug("Get stage failed. Current algorithm is canceled.");
            setAction(AlgorithmExecutingAction.FINISH, params);
            return null;
        }
        if (algorithmExec.stageExecs().isEmpty()) {
            setAction(AlgorithmExecutingAction.FINISH, params);
            return null;
        }
        switch (action) {
            case EXEC_NEXT:
                return getNextAlgorithmStageExec(algorithmExec, params);
            case EXEC_POINT:
                return getPointAlgorithmStageExec(algorithmExec, params);
            default:
                setAction(AlgorithmExecutingAction.FINISH, params);
        }
        return null;
    }

    private AlgorithmStageExec getNextAlgorithmStageExec(@NonNull AlgorithmExec algorithmExec,
                                                         @NonNull Map<String, Object> params) {
        var currentStageOrder = getStageOrderFromParams(algorithmExec, params, CURRENT_STAGE_PARAM_NAME);
        var result =
            selectAlgorithmStageExec(algorithmExec, exec -> exec.getStageDefinition().getOrder() > currentStageOrder);
        checkAndCreateStage(algorithmExec, result, params);
        return result;
    }

    private Integer getStageOrderFromParams(@NonNull AlgorithmExec algorithmExec,
                                            @NonNull Map<String, Object> params,
                                            @NonNull String stageParamName) {
        return
            Optional.ofNullable(params.get(stageParamName))
                .map(Integer.class::cast)
                .orElse(
                    algorithmExec.stageExecs().get(0).getStageDefinition().getOrder() - 1
                );
    }

    private AlgorithmStageExec selectAlgorithmStageExec(@NonNull AlgorithmExec algorithmExec,
                                                        @NonNull Predicate<? super AlgorithmStageExec> predicate) {
        return
            algorithmExec.stageExecs().stream()
                .filter(predicate)
                .min(Comparator.comparingInt(o -> o.getStageDefinition().getOrder()))
                .orElse(null);
    }

    private void checkAndCreateStage(@NonNull AlgorithmExec algorithmExec,
                                     AlgorithmStageExec stageExec,
                                     @NonNull Map<String, Object> params) {
        if (nonNull(stageExec)) {
            if (isNull(stageExec.getStage())) {
                stageExec.setStage(
                    algorithmStageRepository.create(
                        algorithmExec.algorithm(),
                        stageExec.getStageDefinition(),
                        params
                    )
                );
                log.debug("Stage created with id: {}.", stageExec.getStage().getId());
            }
        } else {
            setAction(AlgorithmExecutingAction.FINISH, params);
        }
    }

    private AlgorithmStageExec getPointAlgorithmStageExec(@NonNull AlgorithmExec algorithmExec,
                                                          @NonNull Map<String, Object> params) {
        var nextStageOrder = getStageOrderFromParams(algorithmExec, params, NEXT_STAGE_PARAM_NAME);
        var result =
            selectAlgorithmStageExec(
                algorithmExec,
                exec -> Objects.equals(exec.getStageDefinition().getOrder(), nextStageOrder)
            );
        checkAndCreateStage(algorithmExec, result, params);
        return result;
    }

    private void executeStage(@NonNull AlgorithmExec algorithmExec,
                              @NonNull AlgorithmStageExec stageExec,
                              @NonNull Map<String, Object> params) {
        log.debug("Execute stage algorithm-{} with definition: {}.",
            algorithmExec.algorithm().getId(),
            stageExec.getStageDefinition()
        );
        params.put(CURRENT_STAGE_PARAM_NAME, stageExec.getStageDefinition().getOrder());
        params.remove(CURRENT_STEP_PARAM_NAME);
        var action = getAction(params);
        var stepExec = getAlgorithmStepExec(stageExec, params, action);
        try {
            while (nonNull(stepExec) && !AlgorithmExecutingAction.FINISH.equals(action)) {
                log.debug("Execute step order: {}", stepExec.getStepDefinition().getOrder());
                Throwable throwable = null;
                if (params.containsKey(ERROR_STEP_PARAM_NAME)) {
                    throwable = (Throwable) params.get(ERROR_STEP_PARAM_NAME);
                    params.remove(ERROR_STEP_PARAM_NAME);
                }
                executeStep(stageExec, stepExec, params);
                if (nonNull(throwable)) {
                    params.put(ERROR_STEP_PARAM_NAME, throwable);
                }
                action = getAction(params);
                stepExec = getAlgorithmStepExec(stageExec, params, action);
                action = getAction(params);
            }
            log.debug("Finish execute stage-{} algorithm-{}.",
                stageExec.getStage().getId(),
                algorithmExec.algorithm().getId()
            );
        } catch (Exception exception) {
            log.debug("Finish execute stage algorithm-{} with error.", algorithmExec.algorithm().getId());
            throw exception;
        } finally {
            algorithmStageRepository.finish(stageExec.getStage().getId(), params);
        }
    }

    private AlgorithmStepExec getAlgorithmStepExec(@NonNull AlgorithmStageExec stageExec,
                                                   @NonNull Map<String, Object> params,
                                                   @NonNull AlgorithmExecutingAction action) {
        if (checkCancelTaskService.isTaskCanceled(params)) {
            log.debug("Get step failed. Current algorithm is canceled.");
            setAction(AlgorithmExecutingAction.FINISH, params);
            return null;
        }
        if (stageExec.getStepExecs().isEmpty() || nonCurrentStageExecuting(stageExec, params)) {
            return null;
        }
        switch (action) {
            case EXEC_NEXT:
                return getNextAlgorithmStepExec(stageExec, params);
            case EXEC_POINT:
                return getPointAlgorithmStepExec(stageExec, params);
            default:
                setAction(AlgorithmExecutingAction.FINISH, params);
        }
        return null;
    }

    private boolean nonCurrentStageExecuting(@NonNull AlgorithmStageExec stageExec,
                                             @NonNull Map<String, Object> params) {
        return
            !Optional.ofNullable(params.get(CURRENT_STAGE_PARAM_NAME))
                .map(Integer.class::cast)
                .map(stageExec.getStageDefinition().getOrder()::equals)
                .orElse(true);
    }

    private AlgorithmStepExec getNextAlgorithmStepExec(@NonNull AlgorithmStageExec stageExec,
                                                       @NonNull Map<String, Object> params) {
        var currentStepOrder = getStepOrderFromParams(stageExec, params, CURRENT_STEP_PARAM_NAME);
        var result =
            selectAlgorithmStepExec(stageExec, exec -> exec.getStepDefinition().getOrder() > currentStepOrder);
        checkAndCreateStep(stageExec, result, params);
        return result;
    }

    private Integer getStepOrderFromParams(@NonNull AlgorithmStageExec stageExec,
                                           @NonNull Map<String, Object> params,
                                           @NonNull String stepParamName) {
        return
            Optional.ofNullable(params.get(stepParamName))
                .map(Integer.class::cast)
                .orElse(
                    stageExec.getStepExecs().get(0).getStepDefinition().getOrder() - 1
                );
    }

    private AlgorithmStepExec selectAlgorithmStepExec(@NonNull AlgorithmStageExec stageExec,
                                                      @NonNull Predicate<? super AlgorithmStepExec> predicate) {
        return
            stageExec.getStepExecs().stream()
                .filter(predicate)
                .min(Comparator.comparingInt(o -> o.getStepDefinition().getOrder()))
                .orElse(null);
    }

    private void checkAndCreateStep(@NonNull AlgorithmStageExec stageExec,
                                    AlgorithmStepExec stepExec,
                                    @NonNull Map<String, Object> params) {
        if (nonNull(stepExec)) {
            stepExec.setStep(
                algorithmStepRepository.create(stageExec.getStage(), stepExec.getStepDefinition(), params)
            );
            log.debug("Step created with id: {}.", stepExec.getStep().getId());
        }
    }

    private AlgorithmStepExec getPointAlgorithmStepExec(@NonNull AlgorithmStageExec stageExec,
                                                        @NonNull Map<String, Object> params) {
        AlgorithmStepExec result = null;
        if (nonStageChange(stageExec, params)) {
            var nextStepOrder = getStepOrderFromParams(stageExec, params, NEXT_STEP_PARAM_NAME);
            result =
                selectAlgorithmStepExec(
                    stageExec,
                    exec -> Objects.equals(exec.getStepDefinition().getOrder(), nextStepOrder)
                );
            checkAndCreateStep(stageExec, result, params);
        }
        return result;
    }

    private boolean nonStageChange(@NonNull AlgorithmStageExec stageExec,
                                   @NonNull Map<String, Object> params) {
        return
            Objects.equals(
                stageExec.getStageDefinition().getOrder(),
                params.get(NEXT_STAGE_PARAM_NAME)
            );
    }

    private void executeStep(@NonNull AlgorithmStageExec stageExec,
                             @NonNull AlgorithmStepExec stepExec,
                             @NonNull Map<String, Object> params) {
        log.debug(
            "Start execute step stage-{} algorithm-{} with definition: {}.",
            stageExec.getStage().getId(),
            stageExec.getStage().getAlgorithm().getId(),
            stepExec.getStepDefinition()
        );
        try {
            params.put(P_ID_STEP, stepExec.getStep().getId());
            setAction(AlgorithmExecutingAction.EXEC_NEXT, params);
            params.put(CURRENT_STEP_PARAM_NAME, stepExec.getStepDefinition().getOrder());
            params.remove(NEXT_STAGE_PARAM_NAME);
            params.remove(NEXT_STEP_PARAM_NAME);
            stepExec.getExecutor().execute(params);
            var throwable = (Throwable) params.get(ERROR_STEP_PARAM_NAME);
            if (!params.containsKey(NEXT_STEP_PARAM_NAME) && nonNull(throwable)) {
                throw new StepExecuteErrorException(throwable, stepExec.getStepDefinition().toString());
            }
            log.debug(
                "Finish execute step-{} stage-{} algorithm-{}.",
                stepExec.getStep().getId(),
                stageExec.getStage().getId(),
                stageExec.getStage().getAlgorithm().getId()
            );
        } catch (Exception exception) {
            log.error("Finish execute step stage-{} algorithm-{} with error.",
                stageExec.getStage().getId(),
                stageExec.getStage().getAlgorithm().getId(),
                exception
            );
            if (isNull(params.get(ERROR_STEP_PARAM_NAME))) {
                params.put(ERROR_STEP_PARAM_NAME, exception);
            }
            throw exception;
        } finally {
            algorithmStepRepository.finish(stepExec.getStep().getId(), params);
        }
    }
}
