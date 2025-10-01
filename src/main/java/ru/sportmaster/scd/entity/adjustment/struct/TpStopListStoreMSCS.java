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
@StructType("SCD.SCD_DC_TP_COLL_MSCS_SHOP_STL")
public class TpStopListStoreMSCS implements ITpAdjustmentRow {
    @Column(name = "id_coll_mscs_shop_stl")
    private Long id;
    @Column(name = "id_collection")
    private Long collectionId;
    @Column(name = "id_merch_color_size")
    private Long sizeId;
    @Column(name = "id_department")
    private Long departmentId;
    @Column(name = "stl_type")
    private Integer stlType;
}
