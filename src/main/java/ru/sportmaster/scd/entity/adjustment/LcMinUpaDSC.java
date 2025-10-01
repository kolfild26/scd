package ru.sportmaster.scd.entity.adjustment;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.sportmaster.scd.entity.IEntity;
import ru.sportmaster.scd.entity.dictionary.CategoryHierarchy;
import ru.sportmaster.scd.entity.dictionary.Collection;
import ru.sportmaster.scd.entity.pivot.LocationDepartment;
import ru.sportmaster.scd.service.adjustment.ui.AdjustmentDocRowEditor;
import ru.sportmaster.scd.ui.resolver.JpaViewResolver;
import ru.sportmaster.scd.ui.view.annotation.CustomView;
import ru.sportmaster.scd.ui.view.annotation.View;
import ru.sportmaster.scd.ui.view.annotation.ViewField;
import ru.sportmaster.scd.ui.view.annotation.ViewRenderer;
import ru.sportmaster.scd.ui.view.type.DataFetchType;

@SuppressWarnings("CPD-START")
@CustomView(
    resolver = JpaViewResolver.class,
    editor = AdjustmentDocRowEditor.class,
    fetchType = DataFetchType.INFINITY
)
@Entity
@Table(name = "SCD_DC_V_COLL_DSC_SHOP_CATEGORY_PT_OSPERTYP_VAL", schema = "SCD")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//Hibernate
@Cacheable
@Cache(region = "referenceCache", usage = CacheConcurrencyStrategy.READ_WRITE)
public class LcMinUpaDSC implements IEntity<Long> {
    @Id
    @Column(name = "ID_COLL_DSC_SHOP_CATEGORY_PT_OSPERTYP_VAL")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "ID_COLLECTION")
    @View(flat = true)
    @ViewField(renderer = @ViewRenderer(srcField = "name"))
    private Collection collection;
    @ManyToOne
    @JoinColumn(name = "ID_DEV_STYLECOLOR")
    @View(flat = true)
    @ViewField
    private DevColorModel model;
    @ManyToOne
    @JoinColumn(name = "ID_DEPARTMENT")
    @View(flat = true)
    @ViewField(renderer = @ViewRenderer(srcField = "name"))
    private LocationDepartment shop;
    @ManyToOne
    @JoinColumn(name = "ID_CATEGORY_HIERARCHY")
    @View(flat = true)
    @ViewField(renderer = @ViewRenderer(srcField = "name"))
    private CategoryHierarchy category;
    @Column(name = "UPA_TYPE")
    @ViewField
    private String presentationType;
    @ManyToOne
    @JoinColumn(name = "ID_PERIOD_TYPE")
    @View(flat = true)
    @ViewField(renderer = @ViewRenderer(srcField = "name"))
    private OsLifecycleStage periodType;
    @Column(name = "VAL")
    @ViewField
    private Integer value;
    @ManyToOne
    @JoinColumn(name = "ID_CORRECTION_LIST")
    private AdjustmentDoc document;
}
