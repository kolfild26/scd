package ru.sportmaster.scd.entity.replenishment;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Cacheable;
import jakarta.persistence.CascadeType;
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
import ru.sportmaster.scd.persistence.ExistingOrSequenceIdGenerator;
import ru.sportmaster.scd.ui.view.annotation.View;
import ru.sportmaster.scd.ui.view.annotation.ViewField;
import ru.sportmaster.scd.ui.view.annotation.ViewRenderer;

@View
@Entity
@Table(name = "SCD_REPL_STRATEGY_NODES_LINK", schema = "SCD")
//Lombok
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
//Hibernate
@Cacheable
@Cache(region = "referenceCache", usage = CacheConcurrencyStrategy.READ_WRITE)
public class ReplenishmentStrategyNode {
    @Id
    @GenericGenerator(
        name = "replStrategyNodesSequence",
        type = ExistingOrSequenceIdGenerator.class,
        parameters = {
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "SCD.SEQ_REPL_STRATEGY_NODES_LINK"),
            @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
            @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),
            @org.hibernate.annotations.Parameter(name = "optimizer", value = "pooled-lo")
        }
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "replStrategyNodesSequence")
    @ViewField(editable = false)
    @Column(name = "ID_STRATEGY_NODE")
    private Long id;
    @ViewField
    @Column(name = "DATE_STRAT_BEGIN")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate beginDate;
    @ViewField
    @Column(name = "DATE_STRAT_END")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate endDate;
    @ViewField(renderer = @ViewRenderer(srcField = "name"))
    @ManyToOne
    @JoinColumn(name = "ID_REPL_STRATEGY")
    private ReplenishmentStrategy strategy;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ID_REPL_STRATEGY_CHAIN")
    private ReplenishmentStrategyChain chain;
    @ManyToOne
    @JoinColumn(name = "ID_REPL_INTERVAL")
    private ReplenishmentStrategyInterval interval;
}
