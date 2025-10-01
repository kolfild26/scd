package ru.sportmaster.scd.mapper.struct;

import static java.util.Objects.nonNull;

import java.util.Optional;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.dto.pivot.PivotColumnDto;
import ru.sportmaster.scd.entity.struct.TpPivotColumn;
import ru.sportmaster.scd.struct.IDtoMapper;
import ru.sportmaster.scd.struct.StructMapper;
import ru.sportmaster.scd.ui.view.type.LocalizedProperty;

@Component
public class TpPivotColumnMapper
    extends StructMapper<TpPivotColumn>
    implements IDtoMapper<TpPivotColumn, PivotColumnDto> {
    public TpPivotColumnMapper() {
        super(TpPivotColumn.class);
    }

    @Override
    public TpPivotColumn fromDto(PivotColumnDto source) {
        return
            Optional.ofNullable(source)
                .map(column ->
                        TpPivotColumn.builder()
                            .key(column.getKey())
                            .title(column.getTitle().getFirstKey())
                            .isHidden(Boolean.TRUE.equals(column.getHidden()) ? 1 : null)
                            .childrens(
                                Optional.ofNullable(column.getChildren())
                                    .map(columns ->
                                        columns.stream()
                                            .map(this::fromDto)
                                            .toList()
                                    )
                                    .orElse(null)
                            )
                            .columnFilterType(source.getColumnFilterType())
                            .isFixed(Boolean.TRUE.equals(column.getIsFixed()) ? 1 : null)
                            .build()
                    )
                .orElse(null);
    }

    @Override
    public PivotColumnDto toDto(TpPivotColumn source) {
        return
            Optional.ofNullable(source)
                .map(column ->
                    PivotColumnDto.builder()
                        .key(column.getKey())
                        .title(LocalizedProperty.of(column.getTitle()))
                        .hidden(nonNull(column.getIsHidden()) && column.getIsHidden() == 1)
                        .children(
                            Optional.ofNullable(column.getChildrens())
                                .map(columns ->
                                    columns.stream()
                                        .map(this::toDto)
                                        .toList()
                                )
                                .orElse(null)
                        )
                        .columnFilterType(source.getColumnFilterType())
                        .isFixed(nonNull(column.getIsFixed()) && column.getIsFixed() == 1)
                        .build()
                )
                .orElse(null);
    }
}
