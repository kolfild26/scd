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
@StructType(value = "TP_LONG_MAP_ENTRY", arrayName = "TP_LONG_MAP")
public class TpLongMapEntry {
    @Column(name = "v_key")
    private String key;

    @Column(name = "v_longs")
    private List<Long> longs;
}
