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
@StructType("SCD.SCD_DC_TP_COLL_DSC_SHOP_REPL_END_DAT")
public class TpReplenishmentEndDateStoreDSC implements ITpAdjustmentRow {
    @Column(name = "id_coll_dsc_shop_repl_end_dat")
    private Long id;
    @Column(name = "id_collection")
    private Long collectionId;
    @Column(name = "id_dev_stylecolor")
    private Long modelId;
    @Column(name = "id_department")
    private Long departmentId;
    @Column(name = "repl_end_dat")
    private LocalDate replEndDate;
}
