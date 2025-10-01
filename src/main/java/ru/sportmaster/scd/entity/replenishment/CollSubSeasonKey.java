package ru.sportmaster.scd.entity.replenishment;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@EqualsAndHashCode
public class CollSubSeasonKey implements Serializable {
    @Serial
    private static final long serialVersionUID = -1565635743674518076L;

    @Column(name = "ID_COLLECTION")
    private Long collectionId;

    @Column(name = "ID_SUBSEASON")
    private Long subSeasonId;

    @Column(name = "ID_DIVISION_TMA")
    private Long divisionId;

    @Column(name = "ID_BUSINESS_TMA")
    private Long businessId;
}
