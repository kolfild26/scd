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
@StructType("SCD.SCD_DC_TP_COLL_MSC_DTMA_REPL_END_DAT")
public class TpReplenishmentEndDateSalesChannelMSC implements ITpAdjustmentRow {
    @Column(name = "id_coll_msc_dtma_repl_end_dat")
    private Long id;
    @Column(name = "id_collection")
    private Long collectionId;
    @Column(name = "id_ware_color_model")
    private Long modelId;
    @Column(name = "id_division_tma")
    private Long divisionId;
    @Column(name = "repl_end_dat")
    private LocalDate replEndDate;
}
