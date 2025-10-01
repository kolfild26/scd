package ru.sportmaster.scd.mapper.struct;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.struct.TpPivotConfigSort;
import ru.sportmaster.scd.struct.IDtoMapper;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpPivotConfigSortMapper
    extends StructMapper<TpPivotConfigSort>
    implements IDtoMapper<List<TpPivotConfigSort>, Map<String, Sort.Direction>> {
    public TpPivotConfigSortMapper() {
        super(TpPivotConfigSort.class);
    }

    @Override
    public List<TpPivotConfigSort> fromDto(Map<String, Sort.Direction> source) {
        return
            Optional.ofNullable(source)
                .map(Map::entrySet)
                .map(set ->
                    set.stream()
                        .map(entry ->
                            TpPivotConfigSort.builder()
                                .key(entry.getKey())
                                .direct(entry.getValue().toString())
                                .build()
                        )
                        .toList())
                .orElse(null);
    }

    @Override
    public Map<String, Sort.Direction> toDto(List<TpPivotConfigSort> source) {
        return
            Optional.ofNullable(source)
                .map(list ->
                    list.stream()
                        .collect(
                            Collectors.toMap(
                                TpPivotConfigSort::getKey,
                                entry -> Sort.Direction.fromString(entry.getDirect())
                            )
                        )
                )
                .orElse(null);
    }
}
