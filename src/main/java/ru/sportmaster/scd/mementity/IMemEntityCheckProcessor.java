package ru.sportmaster.scd.mementity;

import java.util.List;
import java.util.function.Function;
import ru.sportmaster.scd.dto.mementity.MemEntityDto;
import ru.sportmaster.scd.dto.mementity.MemEntitySelectDto;
import ru.sportmaster.scd.dto.view.SelectionRequestDto;
import ru.sportmaster.scd.ui.view.type.ICondition;

public interface IMemEntityCheckProcessor<K, T> {
    MemEntityDto<T> process(String sessionUuid, K key, T value);

    MemEntitySelectDto<K, T> get(String sessionUuid, String formUuid, List<K> keys);

    MemEntitySelection<K> getMemEntitySelection(String formUuid);

    SelectionRequestDto exec(String sessionUuid,
                             String formUuid,
                             SelectionRequestDto request,
                             Function<List<ICondition>, List<Object>> conditionFilter);

    boolean nonRestoreNeeded(String sessionUuid);

    void clear(String formUuid);
}
