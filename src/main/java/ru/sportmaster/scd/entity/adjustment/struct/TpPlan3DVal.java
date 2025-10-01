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
@StructType("SCD.SCD_DC_TP_COLL_SOP_SHOP_VAL")
public class TpPlan3DVal implements ITpAdjustmentRow {
    @Column(name = "id_coll_sop_shop_val")
    private Long id;
    @Column(name = "id_collection")
    private Long collectionId;
    @Column(name = "id_node")
    private Long nodeId;
    @Column(name = "id_department")
    private Long departmentId;
    @Column(name = "val")
    private Integer val;
}
