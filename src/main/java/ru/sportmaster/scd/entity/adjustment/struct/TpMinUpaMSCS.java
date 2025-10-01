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
@StructType("SCD.SCD_DC_TP_COLL_MSCS_SHOP_CATEGORY_PT_MIN_UPA")
public class TpMinUpaMSCS implements ITpAdjustmentRow {
    @Column(name = "id_coll_mscs_shop_category_pt_min_upa")
    private Long id;
    @Column(name = "id_collection")
    private Long collectionId;
    @Column(name = "id_merch_color_size")
    private Long sizeId;
    @Column(name = "id_department")
    private Long departmentId;
    @Column(name = "id_category_hierarchy")
    private Long categoryId;
    @Column(name = "upa_type")
    private String updType;
    @Column(name = "min_upa")
    private Integer minUpa;
}
