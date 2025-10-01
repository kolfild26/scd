package ru.sportmaster.scd.entity.adjustment.struct;

import jakarta.persistence.Column;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sportmaster.scd.struct.annotation.StructType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@StructType("SCD.SCD_DC_TP_SHOP_OPCL_DAT")
public class TpStoreOpenStoreCloseDate implements ITpAdjustmentRow {
    @Column(name = "id_shop_opcl_dat")
    private Long id;
    @Column(name = "id_department")
    private Long departmentId;
    @Column(name = "open_dat")
    private LocalDate openDate;
    @Column(name = "close_dat")
    private LocalDate closeDate;
}
