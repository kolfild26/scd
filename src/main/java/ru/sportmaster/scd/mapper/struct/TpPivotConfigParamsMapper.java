package ru.sportmaster.scd.mapper.struct;

import java.util.Optional;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.dto.pivot.PivotConfigParametersDto;
import ru.sportmaster.scd.entity.struct.TpPivotConfigParams;
import ru.sportmaster.scd.struct.IDtoMapper;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpPivotConfigParamsMapper
    extends StructMapper<TpPivotConfigParams>
    implements IDtoMapper<TpPivotConfigParams, PivotConfigParametersDto> {
    private final TpPivotConfigHierarchyItemMapper hierarchyItemMapper;
    private final TpLongMapEntryMapper longMapEntryMapper;
    private final TpPivotConfigSortMapper sortMapper;

    public TpPivotConfigParamsMapper(TpPivotConfigHierarchyItemMapper hierarchyItemMapper,
                                     TpLongMapEntryMapper longMapEntryMapper,
                                     TpPivotConfigSortMapper sortMapper) {
        super(TpPivotConfigParams.class);
        this.hierarchyItemMapper = hierarchyItemMapper;
        this.longMapEntryMapper = longMapEntryMapper;
        this.sortMapper = sortMapper;
    }

    @Override
    public TpPivotConfigParams fromDto(PivotConfigParametersDto source) {
        return
            Optional.ofNullable(source)
                .map(params ->
                    TpPivotConfigParams.builder()
                        .hierarchy(
                            Optional.ofNullable(source.getHierarchy())
                                .map(hierarchy ->
                                    hierarchy.stream()
                                        .map(hierarchyItemMapper::fromDto)
                                        .toList()
                                )
                                .orElse(null)
                        )
                        .filters(
                            Optional.ofNullable(source.getFilters())
                                .map(longMapEntryMapper::fromDto)
                                .orElse(null)
                        )
                        .sorts(
                            Optional.ofNullable(source.getSort())
                                .map(sortMapper::fromDto)
                                .orElse(null)
                        )
                        .columnsFilter(source.getConditions())
                        .build()
                )
                .orElse(null);
    }

    @Override
    public PivotConfigParametersDto toDto(TpPivotConfigParams source) {
        return
            Optional.ofNullable(source)
                .map(params ->
                    PivotConfigParametersDto.builder()
                        .hierarchy(
                            Optional.ofNullable(source.getHierarchy())
                                .map(hierarchy ->
                                    hierarchy.stream()
                                        .map(hierarchyItemMapper::toDto)
                                        .toList()
                                )
                                .orElse(null)
                        )
                        .filters(
                            Optional.ofNullable(source.getFilters())
                                .map(longMapEntryMapper::toDto)
                                .orElse(null)
                        )
                        .sort(
                            Optional.ofNullable(source.getSorts())
                                .map(sortMapper::toDto)
                                .orElse(null)
                        )
                        .conditions(source.getColumnsFilter())
                        .build()
                )
                .orElse(null);
    }
}
