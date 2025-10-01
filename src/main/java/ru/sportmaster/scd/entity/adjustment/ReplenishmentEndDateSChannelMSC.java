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
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.sportmaster.scd.entity.IEntity;
import ru.sportmaster.scd.entity.dictionary.Collection;
import ru.sportmaster.scd.entity.pivot.DivisionTMA;
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
@Table(name = "SCD_DC_V_COLL_MSC_DTMA_REPL_END_DAT", schema = "SCD")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//Hibernate
@Cacheable
@Cache(region = "referenceCache", usage = CacheConcurrencyStrategy.READ_WRITE)
public class ReplenishmentEndDateSChannelMSC implements IEntity<Long> {
    @Id
    @Column(name = "ID_COLL_MSC_DTMA_REPL_END_DAT")
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
    @JoinColumn(name = "ID_DIVISION_TMA")
    @View(flat = true)
    @ViewField(renderer = @ViewRenderer(srcField = "name"))
    private DivisionTMA division;
    @Column(name = "REPL_END_DAT")
    @ViewField(rules = @ViewFieldRule(calendarType = CalendarEditorType.WEEK))
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate value;
    @ManyToOne
    @JoinColumn(name = "ID_CORRECTION_LIST")
    private AdjustmentDoc document;
}
