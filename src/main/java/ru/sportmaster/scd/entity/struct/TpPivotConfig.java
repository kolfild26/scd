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
@StructType("TP_PIVOT_CONFIG")
public class TpPivotConfig {
    @Column(name = "v_levels")
    private List<String> levels;

    @Column(name = "v_columns")
    private List<TpPivotColumn> columns;

    @Column(name = "v_params")
    private TpPivotConfigParams params;

    @Column(name = "v_total")
    private Long total;

    @Column(name = "v_is_empty")
    private Integer isEmpty;
}
