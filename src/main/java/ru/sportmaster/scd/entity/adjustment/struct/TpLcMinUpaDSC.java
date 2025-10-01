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
@StructType("SCD.SCD_DC_TP_COLL_DSC_SHOP_CATEGORY_PT_OSPERTYP_VAL")
public class TpLcMinUpaDSC implements ITpAdjustmentRow {
    @Column(name = "id_coll_dsc_shop_category_pt_ospertyp_val")
    private Long id;
    @Column(name = "id_collection")
    private Long collectionId;
    @Column(name = "id_dev_stylecolor")
    private Long modelId;
    @Column(name = "id_department")
    private Long departmentId;
    @Column(name = "id_category_hierarchy")
    private Long categoryId;
    @Column(name = "upa_type")
    private String updType;
    @Column(name = "id_period_type")
    private Long periodTypeId;
    @Column(name = "val")
    private Integer val;
}
