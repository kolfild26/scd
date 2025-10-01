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
@StructType("SCD.SCD_DC_TP_COLL_DSCS_SHOP_MIN_UPA_INEX_DAT_STRTL")
public class TpStartListStoreDSCS implements ITpAdjustmentRow {
    @Column(name = "id_coll_dscs_shop_min_upa_inex_dat_strtl")
    private Long id;
    @Column(name = "id_collection")
    private Long collectionId;
    @Column(name = "id_dev_stylecolor_size")
    private Long sizeId;
    @Column(name = "id_department")
    private Long departmentId;
    @Column(name = "min_upa")
    private Integer minUpa;
    @Column(name = "intake_dat")
    private LocalDate intakeDate;
    @Column(name = "exit_dat")
    private LocalDate exitDate;
}
