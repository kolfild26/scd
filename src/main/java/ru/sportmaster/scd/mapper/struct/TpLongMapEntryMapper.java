package ru.sportmaster.scd.mapper.struct;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.struct.TpLongMapEntry;
import ru.sportmaster.scd.struct.IDtoMapper;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpLongMapEntryMapper
    extends StructMapper<TpLongMapEntry>
    implements IDtoMapper<List<TpLongMapEntry>, Map<String, List<Long>>> {
    public TpLongMapEntryMapper() {
        super(TpLongMapEntry.class);
    }

    @Override
    public List<TpLongMapEntry> fromDto(Map<String, List<Long>> source) {
        return
            Optional.ofNullable(source)
                .map(Map::entrySet)
                .map(set ->
                    set.stream()
                        .map(entry ->
                            TpLongMapEntry.builder()
                                .key(entry.getKey())
                                .longs(entry.getValue())
                                .build()
                        )
                        .toList())
                .orElse(null);
    }

    @Override
    public Map<String, List<Long>> toDto(List<TpLongMapEntry> source) {
        return
            Optional.ofNullable(source)
                .map(list ->
                    list.stream()
                        .collect(
                            Collectors.toMap(
                                TpLongMapEntry::getKey,
                                TpLongMapEntry::getLongs
                            )
                        )
                )
                .orElse(null);
    }
}
