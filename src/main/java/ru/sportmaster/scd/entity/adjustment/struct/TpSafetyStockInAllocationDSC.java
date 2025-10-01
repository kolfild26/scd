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
@StructType("SCD.SCD_DC_TP_COLL_DSC_SHOP_SAFETY_STOCK_AL")
public class TpSafetyStockInAllocationDSC implements ITpAdjustmentRow {
    @Column(name = "id_coll_dsc_shop_safety_stock_al")
    private Long id;
    @Column(name = "id_collection")
    private Long collectionId;
    @Column(name = "id_dev_stylecolor")
    private Long modelId;
    @Column(name = "id_department")
    private Long departmentId;
    @Column(name = "stop_list")
    private Integer value;
    @Column(name = "safety_stock_al")
    private Integer safStockAllocation;
}
