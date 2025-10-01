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
@StructType("SCD.SCD_DC_TP_COLL_PRD_MSC_SHOP_REPL_STR")
public class TpReplenishmentStrategyMSC implements ITpAdjustmentRow {
    @Column(name = "id_coll_prd_msc_shop_repl_str")
    private Long id;
    @Column(name = "id_collection")
    private Long collectionId;
    @Column(name = "id_strat_man_begin")
    private Long startDayId;
    @Column(name = "id_strat_man_end")
    private Long endDayId;
    @Column(name = "id_ware_color_model")
    private Long colorId;
    @Column(name = "id_department")
    private Long departmentId;
    @Column(name = "id_repl_str")
    private Long replStrategyId;
}
