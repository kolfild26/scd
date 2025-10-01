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
@Table(name = "SCD_DC_V_COLL_MSC_SHOP_WSS_PARAMS", schema = "SCD")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//Hibernate
@Cacheable
@Cache(region = "referenceCache", usage = CacheConcurrencyStrategy.READ_WRITE)
public class CoefficientWSSAllocationMSC {
    @Id
    @Column(name = "ID_COLL_MSC_SHOP_WSS_PARAMS")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "ID_COLLECTION")
    @View(flat = true)
    @ViewField(renderer = @ViewRenderer(srcField = "name"))
    private Collection collection;
    @ManyToOne
    @JoinColumn(name = "ID_WARE_COLOR_MODEL")
    @View(flat = true)
    @ViewField
    private MerchColorModel model;
    @ManyToOne
    @JoinColumn(name = "ID_DEPARTMENT")
    @View(flat = true)
    @ViewField(renderer = @ViewRenderer(srcField = "name"))
    private LocationDepartment shop;
    @ViewField(required = false)
    @Column(name = "PERCENT_SS", precision = 4, scale = 3)
    private Double safetyStockPercent;
    @ViewField(required = false)
    @Column(name = "MIN_W_SS", precision = 4)
    private Integer minWeekSafetyStock;
    @ViewField(required = false)
    @Column(name = "MAX_W_SS", precision = 4)
    private Integer maxWeekSafetyStock;
    @ViewField(required = false)
    @Column(name = "PERIOD_W_SS", precision = 4)
    private Integer periodWeekSafetyStock;
    @ManyToOne
    @JoinColumn(name = "ID_CORRECTION_LIST")
    private AdjustmentDoc document;
}
