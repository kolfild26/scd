package ru.sportmaster.scd.entity.adjustment;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "SCD_MDM_DEV_STYLECOLOR", schema = "SCD")
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
public class DevColorModel implements IEntity<Long> {
    @Id
    @ViewField(renderer = @ViewRenderer(updField = "model", updType = DevColorModel.class))
    @Column(name = "ID_DEV_STYLECOLOR", precision = 38, nullable = false, updatable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "ID_DEV_STYLE")
    @View(flat = true)
    @ViewField(renderer = @ViewRenderer(srcField = "code"), editable = false)
    private DevModel model;
    @ViewField(editable = false)
    @Column(name = "COLOR_NAME")
    private String colorName;
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
