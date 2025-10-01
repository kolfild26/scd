package ru.sportmaster.scd.entity.adjustment;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.sportmaster.scd.entity.dictionary.Day;
import ru.sportmaster.scd.entity.dictionary.RDHierarchyGeneral;
import ru.sportmaster.scd.entity.pivot.LocationDepartment;
import ru.sportmaster.scd.service.adjustment.ui.AdjustmentDocRowEditor;
import ru.sportmaster.scd.ui.resolver.JpaViewResolver;
import ru.sportmaster.scd.ui.view.annotation.CalendarEditorType;
import ru.sportmaster.scd.ui.view.annotation.CustomView;
import ru.sportmaster.scd.ui.view.annotation.View;
import ru.sportmaster.scd.ui.view.annotation.ViewField;
import ru.sportmaster.scd.ui.view.annotation.ViewFieldRule;
import ru.sportmaster.scd.ui.view.annotation.ViewRenderer;
import ru.sportmaster.scd.ui.view.type.DataFetchType;

@CustomView(
    resolver = JpaViewResolver.class,
    editor = AdjustmentDocRowEditor.class,
    fetchType = DataFetchType.INFINITY
)
@Entity
@Table(name = "SCD_DC_V_SOP_SHOP_DAYID_MDWGHT", schema = "SCD")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//Hibernate
@Cacheable
@Cache(region = "referenceCache", usage = CacheConcurrencyStrategy.READ_WRITE)
public class ManualDayWeight {
    @Id
    @Column(name = "ID_SOP_SHOP_DAYID_MDWGHT", precision = 38, nullable = false, updatable = false)
    private Long id;
    @ManyToOne
    @JoinColumns(
        foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT),
        value = {
            @JoinColumn(name = "SOP_NODE_ID", referencedColumnName = "ID_NODE"),
            @JoinColumn(name = "SOP_LEVEL_NUM", referencedColumnName = "LEVEL_NUM")
        })
    @View(flat = true)
    @ViewField(renderer = @ViewRenderer(srcField = "moniker"))
    private RDHierarchyGeneral planningRow;
    @ManyToOne
    @JoinColumn(name = "ID_DEPARTMENT")
    @View(flat = true)
    @ViewField(renderer = @ViewRenderer(srcField = "name"))
    private LocationDepartment shop;
    @ManyToOne
    @JoinColumn(name = "ID_DAY")
    @ViewField(
        renderer = @ViewRenderer(srcField = "day", srcType = LocalDate.class, updType = LocalDate.class),
        rules = @ViewFieldRule(calendarType = CalendarEditorType.DAY)
    )
    private Day day;
    @Column(name = "MANUAL_DAY_WEIGHT", precision = 4, scale = 3)
    @ViewField
    private Double value;
    @ManyToOne
    @JoinColumn(name = "ID_CORRECTION_LIST")
    private AdjustmentDoc document;
}
