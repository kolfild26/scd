package ru.sportmaster.scd.mapper.struct;

import java.util.Optional;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.dto.pivot.PivotFilterDto;
import ru.sportmaster.scd.entity.struct.TpPivotFilter;
import ru.sportmaster.scd.struct.IDtoMapper;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpPivotFilterMapper
    extends StructMapper<TpPivotFilter>
    implements IDtoMapper<TpPivotFilter, PivotFilterDto> {
    private final TpLongMapEntryMapper longMapEntryMapper;

    public TpPivotFilterMapper(TpLongMapEntryMapper longMapEntryMapper) {
        super(TpPivotFilter.class);
        this.longMapEntryMapper = longMapEntryMapper;
    }

    @Override
    public TpPivotFilter fromDto(PivotFilterDto source) {
        return
            Optional.ofNullable(source)
                .map(filter ->
                    TpPivotFilter.builder()
                        .name(filter.getName())
                        .filters(longMapEntryMapper.fromDto(filter.getValues()))
                        .build()
                )
                .orElse(null);
    }

    @Override
    public PivotFilterDto toDto(TpPivotFilter source) {
        return
            Optional.ofNullable(source)
                .map(filter ->
                    PivotFilterDto.builder()
                        .name(filter.getName())
                        .values(longMapEntryMapper.toDto(filter.getFilters()))
                        .build()
                )
                .orElse(null);
    }
}
