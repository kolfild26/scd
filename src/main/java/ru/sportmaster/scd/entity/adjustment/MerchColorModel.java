package ru.sportmaster.scd.entity.adjustment;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import ru.sportmaster.scd.entity.IEntity;
import ru.sportmaster.scd.ui.view.annotation.View;
import ru.sportmaster.scd.ui.view.annotation.ViewField;
import ru.sportmaster.scd.ui.view.annotation.ViewRenderer;

@View
@Entity
@Table(name = "SCD_MDM_MERCH_STYLECOLOR", schema = "SCD")
//Lombok
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
//Hibernate
@Immutable
@Cacheable
@Cache(region = "referenceCache", usage = CacheConcurrencyStrategy.READ_WRITE)
public class MerchColorModel implements IEntity<Long> {
    @Id
    @ViewField(renderer = @ViewRenderer(updField = "model", updType = MerchColorModel.class))
    @Column(name = "ID_WARE_COLOR_MODEL", precision = 38, nullable = false, updatable = false)
    private Long id;
    @ViewField(editable = false)
    @Column(name = "CODE")
    private String modelCode;
    @Column(name = "ID_GRP_OF_WARE")
    private String wareGroup;
    @Column(name = "ID_CATEGORY")
    private String category;
    @Column(name = "ID_SUBCATEGORY")
    private String subCategory;
    @Column(name = "ID_GENDER")
    private String gender;
    @Column(name = "ID_AGE")
    private String age;
}
