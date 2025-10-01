package ru.sportmaster.scd.entity.replenishment;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Embeddable
@EqualsAndHashCode
public class KgtSettingsKey implements Serializable {
    private static final long serialVersionUID = -4602859553233145073L;

    @Column(name = "ID_CATEGORY")
    private Long categoryId;

    @Column(name = "ID_SUBCATEGORY")
    private Long subCategoryId;

    @Column(name = "ID_GRP")
    private Long wareClassId;

    @Column(name = "ID_GRP_OF_WARE")
    private Long wareGroupId;

    @Column(name = "ID_BUSINESS_TMA")
    private Long businessId;
}
