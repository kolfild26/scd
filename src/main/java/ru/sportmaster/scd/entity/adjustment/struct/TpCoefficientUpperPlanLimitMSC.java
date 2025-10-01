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
@StructType("SCD.SCD_DC_TP_COLL_MSC_SHOP_UPPER_PLAN_LIM")
public class TpCoefficientUpperPlanLimitMSC implements ITpAdjustmentRow {
    @Column(name = "id_coll_msc_shop_upper_plan_lim")
    private Long id;
    @Column(name = "id_collection")
    private Long collectionId;
    @Column(name = "id_ware_color_model")
    private Long modelId;
    @Column(name = "id_department")
    private Long departmentId;
    @Column(name = "upper_plan_lim")
    private Integer coefUpperPlan;
}
