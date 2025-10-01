package ru.sportmaster.scd.mapper.struct;

import java.util.Optional;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.dto.pivot.PivotConfigDto;
import ru.sportmaster.scd.entity.struct.TpPivotConfig;
import ru.sportmaster.scd.struct.IDtoMapper;
import ru.sportmaster.scd.struct.StructMapper;
import ru.sportmaster.scd.ui.view.type.LocalizedProperty;

@Component
public class TpPivotConfigMapper
    extends StructMapper<TpPivotConfig>
    implements IDtoMapper<TpPivotConfig, PivotConfigDto> {
    private final TpPivotColumnMapper columnMapper;
    private final TpPivotConfigParamsMapper paramsMapper;

    public TpPivotConfigMapper(TpPivotColumnMapper columnMapper,
                               TpPivotConfigParamsMapper paramsMapper) {
        super(TpPivotConfig.class);
        this.columnMapper = columnMapper;
        this.paramsMapper = paramsMapper;
    }

    @Override
    public TpPivotConfig fromDto(PivotConfigDto source) {
        return
            Optional.ofNullable(source)
                .map(config ->
                    TpPivotConfig.builder()
                        .levels(config.getLevels().stream().map(LocalizedProperty::getLastKey).toList())
                        .columns(
                            Optional.ofNullable(config.getColumns())
                                .map(columns ->
                                    columns.stream()
                                        .map(columnMapper::fromDto)
                                        .toList()
                                )
                                .orElse(null)
                        )
                        .params(paramsMapper.fromDto(config.getParameters()))
                        .total(source.getTotal())
                        .isEmpty(Boolean.TRUE.equals(source.getIsEmpty()) ? 1 : null)
                        .build()
                )
                .orElse(null);
    }

    @Override
    public PivotConfigDto toDto(TpPivotConfig source) {
        return
            Optional.ofNullable(source)
                .map(config ->
                    PivotConfigDto.builder()
                        .levels(config.getLevels().stream().map(LocalizedProperty::new).toList())
                        .columns(
                            Optional.ofNullable(config.getColumns())
                                .map(columns ->
                                    columns.stream()
                                        .map(columnMapper::toDto)
                                        .toList()
                                )
                                .orElse(null)
                        )
                        .parameters(paramsMapper.toDto(config.getParams()))
                        .total(source.getTotal())
                        .isEmpty(source.getIsEmpty() == 1)
                        .build()
                )
                .orElse(null);
    }
}
