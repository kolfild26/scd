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
@StructType("SCD.SCD_DC_TP_DSCS_COLL_SALESRGN_DTMATS_PS_VOLUME")
public class TpLcSlsrgnBsnssStReptypeDSCS implements ITpAdjustmentRow {
    @Column(name = "id_dscs_coll_salesrgn_dtmats_ps_volume")
    private Long id;
    @Column(name = "id_dev_stylecolor_size")
    private Long sizeId;
    @Column(name = "id_collection")
    private Long collectionId;
    @Column(name = "id_sales_region")
    private Long countryGroup;
    @Column(name = "id_division_tma_source")
    private Long divisionSourceId;
    @Column(name = "id_division_tma_target")
    private Long divisionTargetId;
    @Column(name = "replace_type")
    private Long replaceType;
    @Column(name = "ps_volume")
    private Double psVolume;
}
