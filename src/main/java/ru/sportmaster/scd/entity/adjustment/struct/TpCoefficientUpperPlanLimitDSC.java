package ru.sportmaster.scd.entity.adjustment.struct;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sportmaster.scd.struct.annotation.StructType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@StructType("SCD.SCD_DC_TP_COLL_DSC_SHOP_UPPER_PLAN_LIM")
public class TpCoefficientUpperPlanLimitDSC implements ITpAdjustmentRow {
    @Column(name = "id_coll_dsc_shop_upper_plan_lim")
    private Long id;
    @Column(name = "id_collection")
    private Long collectionId;
    @Column(name = "id_dev_stylecolor")
    private Long modelId;
    @Column(name = "id_department")
    private Long departmentId;
    @Column(name = "upper_plan_lim")
    private Integer coefUpperPlan;
}
