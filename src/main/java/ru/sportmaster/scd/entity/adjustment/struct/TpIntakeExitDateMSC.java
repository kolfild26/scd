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
@StructType("SCD.SCD_DC_TP_COLL_MSC_SHOP_INEX_DAT")
public class TpIntakeExitDateMSC implements ITpAdjustmentRow {
    @Column(name = "id_coll_msc_shop_inex_dat")
    private Long id;
    @Column(name = "id_collection")
    private Long collectionId;
    @Column(name = "id_ware_color_model")
    private Long modelId;
    @Column(name = "id_department")
    private Long departmentId;
    @Column(name = "in_dat")
    private LocalDate intakeDate;
    @Column(name = "out_dat")
    private LocalDate exitDate;
}
