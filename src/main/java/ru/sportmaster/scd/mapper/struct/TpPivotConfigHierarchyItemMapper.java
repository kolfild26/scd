package ru.sportmaster.scd.mapper.struct;

import static java.util.Objects.nonNull;

import java.util.Optional;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.dto.pivot.PivotConfigHierarchyItem;
import ru.sportmaster.scd.entity.struct.TpPivotConfigHierarchyItem;
import ru.sportmaster.scd.struct.IDtoMapper;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpPivotConfigHierarchyItemMapper
    extends StructMapper<TpPivotConfigHierarchyItem>
    implements IDtoMapper<TpPivotConfigHierarchyItem, PivotConfigHierarchyItem> {
    public TpPivotConfigHierarchyItemMapper() {
        super(TpPivotConfigHierarchyItem.class);
    }

    @Override
    public TpPivotConfigHierarchyItem fromDto(PivotConfigHierarchyItem source) {
        return
            Optional.ofNullable(source)
                .map(item ->
                    TpPivotConfigHierarchyItem.builder()
                        .key(item.getKey())
                        .isSelected(Boolean.TRUE.equals(item.getIsSelected()) ? 1 : null)
                        .build()
                )
                .orElse(null);
    }

    @Override
    public PivotConfigHierarchyItem toDto(TpPivotConfigHierarchyItem source) {
        return
            Optional.ofNullable(source)
                .map(item ->
                        PivotConfigHierarchyItem.builder()
                            .key(item.getKey())
                            .isSelected(nonNull(item.getIsSelected()) && item.getIsSelected() == 1)
                            .build()
                    )
                .orElse(null);
    }
}
