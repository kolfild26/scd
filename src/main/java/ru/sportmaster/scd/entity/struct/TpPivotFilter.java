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
@StructType(value = "TP_PIVOT_FILTER", arrayName = "TP_PIVOT_FILTERS")
public class TpPivotFilter {
    @Column(name = "v_name")
    private String name;

    @Column(name = "v_filters")
    private List<TpLongMapEntry> filters;
}
