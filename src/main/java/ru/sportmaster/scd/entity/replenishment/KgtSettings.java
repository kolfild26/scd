package ru.sportmaster.scd.entity.replenishment;

import jakarta.persistence.Cacheable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.sportmaster.scd.entity.IEntity;
import ru.sportmaster.scd.entity.adjustment.Business;
import ru.sportmaster.scd.entity.dictionary.Category;
import ru.sportmaster.scd.entity.dictionary.SubCategory;
import ru.sportmaster.scd.entity.dictionary.WareClass;
import ru.sportmaster.scd.entity.dictionary.WareGroup;
import ru.sportmaster.scd.service.replenishment.KgtSettingsEditor;
import ru.sportmaster.scd.ui.resolver.JpaViewResolver;
import ru.sportmaster.scd.ui.view.annotation.CustomView;
import ru.sportmaster.scd.ui.view.annotation.View;
import ru.sportmaster.scd.ui.view.annotation.ViewField;
import ru.sportmaster.scd.ui.view.annotation.ViewRenderer;
import ru.sportmaster.scd.ui.view.type.DataFetchType;

@CustomView(
    fetchType = DataFetchType.INFINITY,
    resolver = JpaViewResolver.class,
    editor = KgtSettingsEditor.class
)
@Entity
@Table(name = "SCD_IS_KGT_SETTINGS", schema = "SCD")
//Lombok
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
//Hibernate
@Cacheable
@Cache(region = "referenceCache", usage = CacheConcurrencyStrategy.READ_WRITE)
public class KgtSettings implements IEntity<KgtSettingsKey> {
    @EmbeddedId
    private KgtSettingsKey id = new KgtSettingsKey();
    @ManyToOne
    @View(flat = true)
    @MapsId(value = "wareGroupId")
    @JoinColumn(name = "ID_GRP_OF_WARE")
    @ViewField(renderer = @ViewRenderer(srcField = "name"))
    private WareGroup wareGroup;
    @ManyToOne
    @View(flat = true)
    @MapsId(value = "categoryId")
    @JoinColumn(name = "ID_CATEGORY")
    @ViewField(renderer = @ViewRenderer(srcField = "name"))
    private Category category;
    @ManyToOne
    @View(flat = true)
    @MapsId(value = "subCategoryId")
    @JoinColumn(name = "ID_SUBCATEGORY")
    @ViewField(renderer = @ViewRenderer(srcField = "name"))
    private SubCategory subCategory;
    @ManyToOne
    @View(flat = true)
    @MapsId(value = "wareClassId")
    @JoinColumn(name = "ID_GRP")
    @ViewField(renderer = @ViewRenderer(srcField = "name"))
    private WareClass wareClass;
    @ManyToOne
    @MapsId(value = "businessId")
    @JoinColumn(name = "ID_BUSINESS_TMA")
    private Business business;
}
