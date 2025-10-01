package ru.sportmaster.scd.mementity;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static ru.sportmaster.scd.consts.ParamNames.USER_ID_FIELD;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import ru.sportmaster.scd.dto.SelectionStatus;
import ru.sportmaster.scd.dto.mementity.MemEntityDto;
import ru.sportmaster.scd.dto.mementity.MemEntitySelectDto;
import ru.sportmaster.scd.dto.view.SelectionCommand;
import ru.sportmaster.scd.dto.view.SelectionRequestDto;
import ru.sportmaster.scd.ui.view.type.ICondition;

@Slf4j
public class MemEntityCheckProcessor<K, T> implements IMemEntityCheckProcessor<K, T> {
    private final Cache<String, MemEntitySelection<K>> mainFormMap;
    private final Cache<String, MemEntitySelection<K>> selectFormMap;
    private final Function<Object, K> keyMapper;
    private final MemEntityCheckProcessorRepository<K> checkProcessorRepository;

    public MemEntityCheckProcessor(Function<Object, K> keyMapper,
                                   MemEntityCheckProcessorRepository<K> checkProcessorRepository,
                                   long expireHours) {
        this.mainFormMap =
            Caffeine.newBuilder()
                .expireAfterWrite(expireHours, TimeUnit.HOURS)
                .build();
        this.selectFormMap =
            Caffeine.newBuilder()
                .expireAfterWrite(expireHours, TimeUnit.HOURS)
                .build();
        this.keyMapper = keyMapper;
        this.checkProcessorRepository = checkProcessorRepository;
    }

    @Override
    public MemEntityDto<T> process(String sessionUuid,
                                   K key,
                                   T value) {
        return
            MemEntityDto.<T>builder()
                .value(value)
                .checked(
                    ofNullable(selectFormMap.getIfPresent(sessionUuid))
                        .map(setChecked -> setChecked.isChecked(key))
                        .orElse(false)
                )
                .build();
    }

    @Override
    public MemEntitySelectDto<K, T> get(String sessionUuid, String formUuid, List<K> keys) {
        if (isNull(keys) || keys.isEmpty()) {
            return MemEntitySelectDto.<K, T>builder().build();
        }

        MemEntitySelection<K> selection = mainFormMap.getIfPresent(formUuid);
        if (isNull(selection)) {
            return MemEntitySelectDto.<K, T>builder().build();
        }

        boolean isSelect = selection.isTypeChecked();
        Set<K> choices = selection.getChoices();

        return MemEntitySelectDto.<K, T>builder()
            .firstValueKey(findFirstKey(keys, choices, isSelect))
            .full(keys.size() == choices.size() || (isSelect && choices.isEmpty()))
            .total(isSelect ? keys.size() - choices.size() : choices.size())
            .selectionStatus(getSelectionStatus(sessionUuid, keys))
            .build();
    }

    @Override
    public MemEntitySelection<K> getMemEntitySelection(String formUuid) {
        return mainFormMap.getIfPresent(formUuid);
    }

    @Override
    public SelectionRequestDto exec(String sessionUuid,
                                    String formUuid,
                                    SelectionRequestDto request,
                                    Function<List<ICondition>, List<Object>> conditionFilter) {
        switch (request.getCommand()) {
            case SAVE -> {
                var selection = selectFormMap.getIfPresent(sessionUuid);
                mainFormMap.put(formUuid, selection);
                selectFormMap.invalidate(sessionUuid);
                request.setCommand(SelectionCommand.SAVE_WBD);
                checkProcessorRepository.saveNum(
                    formUuid,
                    ofNullable(MDC.get(USER_ID_FIELD))
                        .map(Long::parseLong)
                        .orElse(null),
                    selection
                );
            }
            case SAVE_WBD -> {
                mainFormMap.put(formUuid, selectFormMap.getIfPresent(sessionUuid));
                selectFormMap.invalidate(sessionUuid);
            }
            case CLEAR -> selectFormMap.invalidate(sessionUuid);
            case RESTORE -> selectFormMap
                .put(
                    sessionUuid,
                    mainFormMap
                        .get(formUuid, k -> MemEntitySelection.create(keyMapper))
                        .toBuilder()
                        .build()
                );
            case SELECT_ALL -> ofNullable(selectFormMap.getIfPresent(sessionUuid))
                .ifPresent(MemEntitySelection::selectAll);
            case UNSELECT_ALL -> ofNullable(selectFormMap.getIfPresent(sessionUuid))
                .ifPresent(MemEntitySelection::unselectAll);
            case SELECT_SEVERAL -> ofNullable(selectFormMap.getIfPresent(sessionUuid))
                .ifPresent(selection -> selection.select(
                    conditionFilter.apply(request.getConditions()), request.isMultiselect()
                ));
            case UNSELECT_SEVERAL -> ofNullable(selectFormMap.getIfPresent(sessionUuid))
                .ifPresent(selection -> selection.unSelect(conditionFilter.apply(request.getConditions())));
            case SELECT -> ofNullable(selectFormMap.getIfPresent(sessionUuid))
                .ifPresent(selection -> selection.select(request.getIds(), request.isMultiselect()));
            case UNSELECT -> ofNullable(selectFormMap.getIfPresent(sessionUuid))
                .ifPresent(selection -> selection.unSelect(request.getIds()));
            default -> log.debug("Неизвестная команда: {}", request.getCommand());
        }
        return request;
    }

    private K findFirstKey(List<K> keys, Set<K> targets, boolean isSelectType) {
        for (K key : keys) {
            if (targets.isEmpty()) {
                return isSelectType ? key : null;
            }
            if (!isSelectType && targets.contains(key)) {
                return key;
            }
            if (isSelectType && !targets.contains(key)) {
                return key;
            }
        }
        return null;
    }

    @Override
    public boolean nonRestoreNeeded(String sessionUuid) {
        return
            nonNull(selectFormMap.getIfPresent(sessionUuid));
    }

    @Override
    public void clear(String formUuid) {
        mainFormMap.invalidate(formUuid);
    }

    private SelectionStatus getSelectionStatus(String sessionUuid, List<K> keys) {
        var selection = selectFormMap.getIfPresent(sessionUuid);
        if (isNull(selection)) {
            return SelectionStatus.SELECT_NONE;
        }

        boolean isSelect = selection.isTypeChecked();
        Set<K> choices = selection.getChoices();
        boolean isFull = selectAllChoices(choices, keys) || (isSelect && choices.isEmpty());

        if (isFull) {
            return SelectionStatus.SELECT_ALL;
        } else if (!choices.isEmpty()) {
            return SelectionStatus.SELECT_SEVERAL;
        }

        return SelectionStatus.SELECT_NONE;
    }

    private boolean selectAllChoices(Set<K> choices, List<K> keys) {
        if (keys.size() <= choices.size()) {
            return choices.containsAll(keys);
        }
        return false;
    }
}
