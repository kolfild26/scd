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
@StructType("SCD.SCD_DC_TP_COLL_DSC_SHOP_OSPERTYPDT")
public class TpLcDatesDSC implements ITpAdjustmentRow {
    @Column(name = "id_coll_dsc_shop_ospertypdt")
    private Long id;
    @Column(name = "id_collection")
    private Long collectionId;
    @Column(name = "id_dev_stylecolor")
    private Long modelId;
    @Column(name = "id_department")
    private Long departmentId;
    @Column(name = "intake_dat")
    private LocalDate intakeDate;
    @Column(name = "markdown_dat")
    private LocalDate markdownDate;
    @Column(name = "suspended_dat")
    private LocalDate suspendedDate;
    @Column(name = "sale_dat")
    private LocalDate saleDate;
    @Column(name = "exit_dat")
    private LocalDate exitDate;
}
