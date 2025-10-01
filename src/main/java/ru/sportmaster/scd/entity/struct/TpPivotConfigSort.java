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
@StructType(value = "TP_PIVOT_CONFIG_SORT", arrayName = "TP_PIVOT_CONFIG_SORTS")
public class TpPivotConfigSort {
    @Column(name = "v_key")
    private String key;

    @Column(name = "v_direct")
    private String direct;
}
