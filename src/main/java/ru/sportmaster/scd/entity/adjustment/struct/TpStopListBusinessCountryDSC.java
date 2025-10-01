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
@StructType("SCD.SCD_DC_TP_COLL_DSC_COUNTRY_DTMA_STL")
public class TpStopListBusinessCountryDSC implements ITpAdjustmentRow {
    @Column(name = "id_coll_dsc_country_dtma_stl")
    private Long id;
    @Column(name = "id_collection")
    private Long collectionId;
    @Column(name = "id_dev_stylecolor")
    private Long modelId;
    @Column(name = "id_country")
    private Long countryId;
    @Column(name = "id_division_tma")
    private Long divisionId;
    @Column(name = "stl_type")
    private Integer stlType;
}
