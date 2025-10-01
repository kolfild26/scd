package ru.sportmaster.scd.service;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static ru.sportmaster.scd.consts.ParamNames.ALGORITHM_GROUP;
import static ru.sportmaster.scd.consts.ParamNames.CALC_ID;
import static ru.sportmaster.scd.consts.ParamNames.HOST_FIELD;
import static ru.sportmaster.scd.consts.ParamNames.IP_ADDRESS_FIELD;
import static ru.sportmaster.scd.consts.ParamNames.MODULE_FIELD;
import static ru.sportmaster.scd.consts.ParamNames.OS_USER_FIELD;
import static ru.sportmaster.scd.consts.ParamNames.PARTITION_TYPE;
import static ru.sportmaster.scd.consts.ParamNames.REQUEST_UUID_FIELD;
import static ru.sportmaster.scd.consts.ParamNames.STOP_ON_ERROR;
import static ru.sportmaster.scd.consts.ParamNames.USER_ID_FIELD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sportmaster.scd.algorithms.AlgComponentStatus;
import ru.sportmaster.scd.algorithms.ComponentType;
import ru.sportmaster.scd.algorithms.DefinitionType;
import ru.sportmaster.scd.algorithms.IAlgComponent;
import ru.sportmaster.scd.algorithms.IAlgComponentDefine;
import ru.sportmaster.scd.algorithms.information.AlgorithmInfo;
import ru.sportmaster.scd.algorithms.information.AlgorithmStageInfo;
import ru.sportmaster.scd.algorithms.information.AlgorithmStepInfo;
import ru.sportmaster.scd.dto.AlgorithmTaskDto;
import ru.sportmaster.scd.dto.AlgorithmTasksDto;
import ru.sportmaster.scd.dto.PutAlgorithmsResponseDto;
import ru.sportmaster.scd.entity.algorithms.definitions.AlgorithmDefinition;
import ru.sportmaster.scd.entity.algorithms.definitions.AlgorithmStageDefinition;
import ru.sportmaster.scd.entity.algorithms.definitions.AlgorithmStepDefinition;
import ru.sportmaster.scd.exceptions.AlgComponentNotFoundException;
import ru.sportmaster.scd.exceptions.DeletedDefinitionException;
import ru.sportmaster.scd.exceptions.UnknownDefinitionException;
import ru.sportmaster.scd.repository.algorithms.definitions.AlgorithmDefinitionRepository;
import ru.sportmaster.scd.repository.algorithms.execution.AlgorithmRepository;
import ru.sportmaster.scd.repository.algorithms.execution.AlgorithmStageRepository;
import ru.sportmaster.scd.repository.algorithms.execution.AlgorithmStepRepository;
import ru.sportmaster.scd.service.caches.HistoryCacheService;
import ru.sportmaster.scd.service.task.TaskManager;
import ru.sportmaster.scd.task.AlgorithmTask;
import ru.sportmaster.scd.task.QueueTasks;
import ru.sportmaster.scd.web.UserEnvironment;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlgorithmControlServiceImpl implements AlgorithmControlService {
    private final AlgorithmRepository algorithmRepository;
    private final AlgorithmDefinitionRepository algorithmDefinitionRepository;
    private final AlgorithmStageRepository algorithmStageRepository;
    private final AlgorithmStepRepository algorithmStepRepository;
    private final HistoryCacheService historyCacheService;
    private final TaskManager taskManager;
    private final QueueTasks queueTasks;

    @Override
    @Transactional
    public PutAlgorithmsResponseDto putAlgorithms(@NonNull AlgorithmTasksDto algorithmTasks,
                                                  UserEnvironment environment) {
        if (isNull(algorithmTasks.getTasks()) || algorithmTasks.getTasks().isEmpty()) {
            return null;
        }
        log.debug("Get algorithmTasks: {}", algorithmTasks);
        List<Long> algorithms = new ArrayList<>();
        final var groupAlgorithm = queueTasks.getGroupId();
        for (var task : algorithmTasks.getTasks()) {
            var algorithmDefinition = getAlgorithmDefinition(task);
            var params = getParams(task, environment);
            params.put(ALGORITHM_GROUP, groupAlgorithm.toString());
            params.put(CALC_ID, algorithmTasks.getCalcId());
            params.put(STOP_ON_ERROR, task.isStopOnError());
            params.put(PARTITION_TYPE, algorithmDefinition.getPartitionType().ordinal());
            var algorithm = algorithmRepository.create(algorithmDefinition, params);
            log.debug("Algorithm created with id: {}.", algorithm.getId());
            putQueueTask(
                AlgorithmTask.builder()
                    .algorithmId(algorithm.getId())
                    .partitionType(algorithmDefinition.getPartitionType())
                    .partitionId(algorithmDefinition.getPartitionType().getPartitionId(params))
                    .groupAlgorithm(groupAlgorithm)
                    .calcId(algorithmTasks.getCalcId())
                    .isStopOnError(task.isStopOnError())
                    .build()
            );
            algorithms.add(algorithm.getId());
        }
        return PutAlgorithmsResponseDto.builder()
            .groupAlgorithm(String.valueOf(groupAlgorithm))
            .algorithms(algorithms)
            .build();
    }

    private AlgorithmDefinition getAlgorithmDefinition(@NonNull AlgorithmTaskDto task) {
        var algorithmDefinition =
            ofNullable(algorithmDefinitionRepository.findById(task.getAlgorithmDefinitionId()))
                .orElseThrow(() ->
                    new UnknownDefinitionException(
                        task.getAlgorithmDefinitionId(),
                        DefinitionType.ALGORITHM_DEFINITION));
        if (algorithmDefinition.isDelete()) {
            throw new DeletedDefinitionException(algorithmDefinition);
        }
        log.debug("Algorithm definition received: {}", algorithmDefinition);
        return algorithmDefinition;
    }

    private Map<String, Object> getParams(@NonNull AlgorithmTaskDto task,
                                          UserEnvironment environment) {
        Map<String, Object> params = nonNull(task.getParams()) ? task.getParams() : new HashMap<>();
        addServiceInformation(params, environment);
        return params;
    }

    private void addServiceInformation(Map<String, Object> params, UserEnvironment environment) {
        if (nonNull(environment)) {
            params.computeIfAbsent(REQUEST_UUID_FIELD, k -> environment.getRequestUuid());
            params.computeIfAbsent(USER_ID_FIELD, k -> environment.getUserId());
            params.computeIfAbsent(OS_USER_FIELD, k -> environment.getOsUser());
            params.computeIfAbsent(HOST_FIELD, k -> environment.getHost());
            params.computeIfAbsent(IP_ADDRESS_FIELD, k -> environment.getIpAddress());
            params.computeIfAbsent(MODULE_FIELD, k -> environment.getModule());
        }
    }

    private void putQueueTask(@NonNull AlgorithmTask task) {
        queueTasks.addTask(task);
        log.debug("Algorithm {} group {} placed in the queue.", task.getAlgorithmId(), task.getGroupAlgorithm());
    }

    @Override
    @Transactional
    public AlgComponentStatus getAlgorithmStatus(@NonNull Long algorithmId) {
        return AlgComponentStatus.getAlgComponentStatus(algorithmRepository.findById(algorithmId));
    }

    @Override
    @Transactional
    public AlgComponentStatus getAlgStageStatus(@NonNull Long algStageId) {
        return AlgComponentStatus.getAlgComponentStatus(algorithmStageRepository.findById(algStageId));
    }

    @Override
    @Transactional
    public AlgComponentStatus getAlgStepStatus(@NonNull Long algStepId) {
        return AlgComponentStatus.getAlgComponentStatus(algorithmStepRepository.findById(algStepId));
    }

    @Override
    @Transactional
    public AlgorithmInfo getAlgorithmInfo(@NonNull Long algorithmId) {
        var algorithm =
            Optional.ofNullable(algorithmRepository.findById(algorithmId))
                .orElseThrow(() -> new AlgComponentNotFoundException(algorithmId, ComponentType.ALGORITHM));
        var algorithmDefinition =
            Optional.ofNullable(
                    historyCacheService.getEntityVersion(
                        AlgorithmDefinition.class,
                        algorithm.getAlgorithmDefinition().getId(),
                        algorithm.getDefVersion()
                    )
                )
                .orElseThrow(() ->
                    new UnknownDefinitionException(
                        algorithm.getAlgorithmDefinition().getId(),
                        DefinitionType.ALGORITHM_DEFINITION));
        var result = AlgorithmInfo.createAlgorithmInfo(algorithm, algorithmDefinition);
        var algorithmStructure = getAlgorithmStructure(algorithmDefinition);
        for (var stage : algorithm.getAlgorithmStages()) {
            var stageDefine =
                getAlgComponentDefine(stage, AlgorithmStageDefinition.class, algorithmStructure);
            var stageInfo =
                AlgorithmStageInfo.createAlgorithmStageInfo(stage, (AlgorithmStageDefinition) stageDefine);
            result.getStages().add(stageInfo);
            for (var step : stage.getAlgorithmSteps()) {
                var stepDefine =
                    getAlgComponentDefine(step, AlgorithmStepDefinition.class, algorithmStructure);
                stageInfo.getSteps().add(
                    AlgorithmStepInfo.createAlgorithmStepInfo(step, (AlgorithmStepDefinition) stepDefine)
                );
            }
        }
        addNonUseStages(result.getStages(), algorithmStructure);
        result.getStages().sort(this::compareAlgorithmInfo);
        for (var stageInfo : result.getStages()) {
            addNonUseSteps(stageInfo, algorithmStructure);
            stageInfo.getSteps().sort(this::compareAlgorithmInfo);
        }
        return result;
    }

    private Map<Class<?>, Map<Long, DefineComponent>> getAlgorithmStructure(
        @NonNull AlgorithmDefinition algorithmDefinition) {
        Map<Class<?>, Map<Long, DefineComponent>> result = new HashMap<>();
        for (var algorithmStageDefinition : algorithmDefinition.getAlgorithmStageDefinitions()) {
            result.computeIfAbsent(AlgorithmStageDefinition.class, k -> new HashMap<>())
                .put(algorithmStageDefinition.getId(), new DefineComponent(algorithmStageDefinition));
            for (var algorithmStepDefinition : algorithmStageDefinition.getAlgorithmStepDefinitions()) {
                result.computeIfAbsent(AlgorithmStepDefinition.class, k -> new HashMap<>())
                    .put(algorithmStepDefinition.getId(), new DefineComponent(algorithmStepDefinition));
            }
        }
        return result;
    }

    private IAlgComponentDefine getAlgComponentDefine(
        @NonNull IAlgComponent component,
        @NonNull Class<?> componentClass,
        @NonNull Map<Class<?>, Map<Long, DefineComponent>> algorithmStructure) {
        var componentDefineMap = algorithmStructure.get(componentClass);
        if (isNull(componentDefineMap) || componentDefineMap.isEmpty()) {
            return null;
        }
        var defineComponent = componentDefineMap.get(component.getDefine().getId());
        if (isNull(defineComponent)) {
            return null;
        }
        defineComponent.nonUse = false;
        return defineComponent.componentDefine;
    }

    private void addNonUseStages(@NonNull List<AlgorithmStageInfo> algorithmStageInfos,
                                 @NonNull Map<Class<?>, Map<Long, DefineComponent>> algorithmStructure) {
        var algorithmStageDefineMap = algorithmStructure.get(AlgorithmStageDefinition.class);
        if (isNull(algorithmStageDefineMap) || algorithmStageDefineMap.isEmpty()) {
            return;
        }
        for (var algorithmStageDefine :
            algorithmStageDefineMap.values().stream()
                .filter(defineComponent -> defineComponent.nonUse)
                .map(defineComponent -> defineComponent.componentDefine)
                .map(AlgorithmStageDefinition.class::cast)
                .toList()) {
            algorithmStageInfos.add(
                AlgorithmStageInfo.createAlgorithmStageInfo(null, algorithmStageDefine)
            );
        }
    }

    private void addNonUseSteps(@NonNull AlgorithmStageInfo stageInfo,
                                @NonNull Map<Class<?>, Map<Long, DefineComponent>> algorithmStructure) {
        var stageDefinition =
            Optional.ofNullable(algorithmStructure.get(AlgorithmStageDefinition.class))
                .map(map -> map.get(stageInfo.getDefinitionId()))
                .map(defineComponent -> defineComponent.componentDefine)
                .map(AlgorithmStageDefinition.class::cast)
                .orElse(null);
        if (isNull(stageDefinition)) {
            return;
        }
        for (var algorithmStepDefinition : stageDefinition.getAlgorithmStepDefinitions()) {
            Optional.ofNullable(algorithmStructure.get(AlgorithmStepDefinition.class))
                .map(map -> map.get(algorithmStepDefinition.getId()))
                .filter(defineComponent -> defineComponent.nonUse)
                .ifPresent(
                    stepDefinitionInfo ->
                        stageInfo.getSteps().add(
                            AlgorithmStepInfo.createAlgorithmStepInfo(
                                null,
                                (AlgorithmStepDefinition) stepDefinitionInfo.componentDefine)
                        )
                );
        }
    }

    private int compareAlgorithmInfo(AlgorithmStepInfo stepInfo1, AlgorithmStepInfo stepInfo2) {
        int compare = Integer.compare(stepInfo1.getOrder(), stepInfo2.getOrder());
        if (compare != 0) {
            return compare;
        }
        return Long.compare(stepInfo1.getDefinitionId(), stepInfo2.getDefinitionId());
    }

    private static class DefineComponent {
        private final IAlgComponentDefine componentDefine;
        private boolean nonUse = true;

        public DefineComponent(IAlgComponentDefine componentDefine) {
            this.componentDefine = componentDefine;
        }
    }

    @Override
    @Transactional
    public boolean serviceStartExecuteAlgorithms() {
        taskManager.getAndStartTasks();
        return true;
    }
}
