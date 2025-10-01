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
@StructType("SCD.SCD_DC_TP_WEEK_MSC_SHOP_FRCST")
public class TpWeekStoreForecastMSC implements ITpAdjustmentRow {
    @Column(name = "id_week_msc_shop_frcst")
    private Long id;
    @Column(name = "id_week")
    private Long weekId;
    @Column(name = "id_ware_color_model")
    private Long modelId;
    @Column(name = "id_department")
    private Long departmentId;
    @Column(name = "man_frcst")
    private Double manFrcst;
}
