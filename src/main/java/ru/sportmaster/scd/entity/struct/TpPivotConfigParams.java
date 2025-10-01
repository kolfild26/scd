package ru.sportmaster.scd.entity.struct;

import jakarta.persistence.Column;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sportmaster.scd.struct.annotation.StructType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@StructType("TP_PIVOT_CONFIG_PARAMS")
public class TpPivotConfigParams {
    @Column(name = "v_hierarchy")
    private List<TpPivotConfigHierarchyItem> hierarchy;

    @Column(name = "v_filters")
    private List<TpLongMapEntry> filters;

    @Column(name = "v_sorts")
    private List<TpPivotConfigSort> sorts;

    @Column(name = "v_columns_filter")
    private String columnsFilter;
}
