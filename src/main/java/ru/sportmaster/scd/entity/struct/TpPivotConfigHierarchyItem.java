package ru.sportmaster.scd.entity.struct;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sportmaster.scd.struct.annotation.StructType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@StructType(value = "TP_PIVOT_CONFIG_HIERARCHY_ITEM", arrayName = "TP_PIVOT_CONFIG_HIERARCHY_ITEMS")
public class TpPivotConfigHierarchyItem {
    @Column(name = "v_key")
    private String key;

    @Column(name = "v_is_selected")
    private Integer isSelected;
}
