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
@StructType("SCD.SCD_DC_TP_COLL_MSC_SHOP_PT_EXTS_PLAN_LIMIT")
public class TpPlanLimitMSC implements ITpAdjustmentRow {
    @Column(name = "id_coll_msc_shop_pt_exts_plan_limit")
    private Long id;
    @Column(name = "id_collection")
    private Long collectionId;
    @Column(name = "id_ware_color_model")
    private Long modelId;
    @Column(name = "id_department")
    private Long departmentId;
    @Column(name = "id_ext_size")
    private Long extSizeId;
    @Column(name = "upa_type")
    private String upaType;
    @Column(name = "plan_limit")
    private Integer planLimit;
}
