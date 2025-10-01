package ru.sportmaster.scd.entity.adjustment;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.sportmaster.scd.entity.dictionary.Week;
import ru.sportmaster.scd.entity.pivot.LocationDepartment;
import ru.sportmaster.scd.service.adjustment.ui.AdjustmentDocRowEditor;
import ru.sportmaster.scd.ui.resolver.JpaViewResolver;
import ru.sportmaster.scd.ui.view.annotation.CustomView;
import ru.sportmaster.scd.ui.view.annotation.View;
import ru.sportmaster.scd.ui.view.annotation.ViewField;
import ru.sportmaster.scd.ui.view.annotation.ViewRenderer;
import ru.sportmaster.scd.ui.view.type.DataFetchType;

@CustomView(
    resolver = JpaViewResolver.class,
    editor = AdjustmentDocRowEditor.class,
    fetchType = DataFetchType.INFINITY
)
@Entity
@Table(name = "SCD_DC_V_WEEK_MSC_SHOP_FRCST", schema = "SCD")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//Hibernate
@Cacheable
@Cache(region = "referenceCache", usage = CacheConcurrencyStrategy.READ_WRITE)
public class ManualQualGoodsMp {
    @Id
    @Column(name = "ID_WEEK_MSC_SHOP_FRCST")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "ID_WEEK")
    @ViewField(
        editable = false,
        renderer = @ViewRenderer(srcField = "week", srcType = LocalDate.class, updType = LocalDate.class))
    private Week week;
    @ManyToOne
    @JoinColumn(name = "ID_WARE_COLOR_MODEL")
    @View(flat = true)
    @ViewField(editable = false)
    private MerchColorModel model;
    @ManyToOne
    @JoinColumn(name = "ID_DEPARTMENT")
    @View(flat = true)
    @ViewField(editable = false, renderer = @ViewRenderer(srcField = "name"))
    private LocationDepartment shop;
    @Column(name = "MAN_FRCST")
    @ViewField(editable = false)
    private Double value;
    @ManyToOne
    @JoinColumn(name = "ID_CORRECTION_LIST")
    private AdjustmentDoc document;
}
