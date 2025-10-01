package ru.sportmaster.scd.entity.simple3dplan;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Embeddable
@EqualsAndHashCode
public class SopPriceLevelDefKey implements Serializable {
    @Serial
    private static final long serialVersionUID = -6348799851940356056L;

    @Column(name = "ID_NODE")
    private Long nodeId;

    @Column(name = "LEVEL_NUM")
    private Long levelNum;

    @Column(name = "ID_COUNTRY_GROUP")
    private Long countryGroupId;

    @Column(name = "ID_COLLECTION")
    private Long collectionId;

    @Column(name = "ID_PARTITION_DIV_TMA")
    private Long partitionId;

    @Column(name = "ID_PRICE_LEVEL")
    private Long priceLevelId;
}
