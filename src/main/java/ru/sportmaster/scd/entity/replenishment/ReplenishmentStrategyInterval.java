package ru.sportmaster.scd.entity.replenishment;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import ru.sportmaster.scd.entity.IEntity;
import ru.sportmaster.scd.entity.adjustment.Business;
import ru.sportmaster.scd.entity.dictionary.Collection;
import ru.sportmaster.scd.entity.pivot.DivisionTMA;
import ru.sportmaster.scd.persistence.ExistingOrSequenceIdGenerator;
import ru.sportmaster.scd.service.replenishment.ReplenishmentStrategyIntervalEditor;
import ru.sportmaster.scd.ui.resolver.JpaViewResolver;
import ru.sportmaster.scd.ui.view.annotation.CustomView;
import ru.sportmaster.scd.ui.view.annotation.View;
import ru.sportmaster.scd.ui.view.annotation.ViewField;
import ru.sportmaster.scd.ui.view.annotation.ViewRenderer;

@CustomView(
    recoverable = true,
    resolver = JpaViewResolver.class,
    editor = ReplenishmentStrategyIntervalEditor.class
)
@Entity
@Table(name = "SCD_REPL_STRATEGY_INTERVAL", schema = "SCD")
//Lombok
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
//Hibernate
@Cacheable
@Cache(region = "referenceCache", usage = CacheConcurrencyStrategy.READ_WRITE)
public class ReplenishmentStrategyInterval implements IEntity<Long> {
    @Id
    @GenericGenerator(
        name = "replenishmentStrategyIntervalSequence",
        type = ExistingOrSequenceIdGenerator.class,
        parameters = {
            @org.hibernate.annotations.Parameter(
                name = "sequence_name",
                value = "SCD.SEQ_REPL_STRATEGY_INTERVAL"),
            @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
            @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),
            @org.hibernate.annotations.Parameter(name = "optimizer", value = "pooled-lo")
        }
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "replenishmentStrategyIntervalSequence")
    @Column(name = "ID_REPL_INTERVAL")
    private Long id;
    @ViewField(editable = false, order = 0)
    @Column(name = "ORDER_")
    private Long order;
    @View(flat = true)
    @ViewField(renderer = @ViewRenderer(srcField = "name"), editable = false)
    @ManyToOne
    @JoinColumn(name = "ID_BUSINESS_TMA")
    private Business business;
    @ManyToOne
    @View(flat = true)
    @ViewField(renderer = @ViewRenderer(srcField = "name"), editable = false)
    @JoinColumn(name = "ID_DIVISION_TMA")
    private DivisionTMA division;
    @View(flat = true)
    @ViewField(renderer = @ViewRenderer(srcField = "name"), editable = false)
    @ManyToOne
    @JoinColumn(name = "ID_COLLECTION")
    private Collection collection;
    @View(flat = true)
    @ViewField(renderer = @ViewRenderer(srcField = "name"), editable = false)
    @ManyToOne
    @JoinColumn(name = "ID_SUBSEASON")
    private SubSeason subSeason;
    @ViewField
    @Column(name = "LABEL_REPL_INTERVAL")
    private String label;
    @ViewField(editable = false)
    @Column(name = "DATE_STRAT_BEGIN")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate beginDate;
    @ViewField
    @Column(name = "DATE_STRAT_END")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate endDate;
    @ViewField
    @Column(name = "DESCRIPTION_REPL_INTERVAL")
    private String description;
    @Column(name = "IS_DELETED")
    private Boolean isDeleted;
}
