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
@StructType("SCD.SCD_DC_TP_SOP_SHOP_DAYID_MDWGHT")
public class TpManualDayWeight implements ITpAdjustmentRow {
    @Column(name = "id_sop_shop_dayid_mdwght")
    private Long id;
    @Column(name = "id_node")
    private Long nodeId;
    @Column(name = "level_num")
    private Long levelNum;
    @Column(name = "id_department")
    private Long departmentId;
    @Column(name = "id_day")
    private Long dayId;
    @Column(name = "manual_day_weight", precision = 4, scale = 3)
    private Double value;
}
