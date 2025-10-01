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
@StructType("SCD.SCD_DC_TP_COLL_MSC_SHOP_WSS_PARAMS")
public class TpCoefficientWSSAllocationMSC implements ITpAdjustmentRow {
    @Column(name = "id_coll_msc_shop_wss_params")
    private Long id;
    @Column(name = "id_collection")
    private Long collectionId;
    @Column(name = "id_ware_color_model")
    private Long modelId;
    @Column(name = "id_department")
    private Long departmentId;
    @Column(name = "percent_ss")
    private Double percentSafStock;
    @Column(name = "min_w_ss")
    private Double minWeekSafStock;
    @Column(name = "max_w_ss")
    private Double maxWeekSafStock;
    @Column(name = "period_w_ss")
    private Double periodWeekSafStock;
}
