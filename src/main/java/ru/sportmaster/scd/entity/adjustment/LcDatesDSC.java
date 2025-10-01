package ru.sportmaster.scd.entity.adjustment;

import com.fasterxml.jackson.annotation.JsonFormat;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.sportmaster.scd.entity.IEntity;
import ru.sportmaster.scd.entity.dictionary.Collection;
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
@Table(name = "SCD_DC_V_COLL_DSC_SHOP_OSPERTYPDT", schema = "SCD")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
//Hibernate
@Cacheable
@Cache(region = "referenceCache", usage = CacheConcurrencyStrategy.READ_WRITE)
public class LcDatesDSC implements IEntity<Long> {
    @Id
    @Column(name = "ID_COLL_DSC_SHOP_OSPERTYPDT")
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
    @Column(name = "INTAKE_DAT")
    @ViewField(rules = @ViewFieldRule(calendarType = CalendarEditorType.WEEK))
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate intakeDate;
    @Column(name = "MARKDOWN_DAT")
    @ViewField(rules = @ViewFieldRule(calendarType = CalendarEditorType.WEEK))
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate markdownDate;
    @Column(name = "SUSPENDED_DAT")
    @ViewField(rules = @ViewFieldRule(calendarType = CalendarEditorType.WEEK))
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate suspendedDate;
    @Column(name = "SALE_DAT")
    @ViewField(rules = @ViewFieldRule(calendarType = CalendarEditorType.WEEK))
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate saleDate;
    @Column(name = "EXIT_DAT")
    @ViewField(rules = @ViewFieldRule(calendarType = CalendarEditorType.WEEK))
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate exitDate;
    @ManyToOne
    @JoinColumn(name = "ID_CORRECTION_LIST")
    protected AdjustmentDoc document;
}
