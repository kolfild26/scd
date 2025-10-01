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
@StructType(value = "TP_PIVOT_COLUMN", arrayName = "TP_PIVOT_COLUMNS")
public class TpPivotColumn {
    @Column(name = "v_key")
    private String key;

    @Column(name = "v_title")
    private String title;

    @Column(name = "v_is_hidden")
    private Integer isHidden;

    @Column(name = "v_is_search")
    private Integer isSearch;

    @Column(name = "v_children")
    private List<TpPivotColumn> childrens;

    @Column(name = "v_column_filter_type")
    private Integer columnFilterType;

    @Column(name = "v_is_fixed")
    private Integer isFixed;
}
