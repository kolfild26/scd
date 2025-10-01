package ru.sportmaster.scd.entity.replenishment;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Immutable;
import ru.sportmaster.scd.entity.dictionary.Collection;
import ru.sportmaster.scd.persistence.ExistingOrSequenceIdGenerator;
import ru.sportmaster.scd.ui.view.annotation.View;
import ru.sportmaster.scd.ui.view.annotation.ViewField;

@View
@Entity
@Table(name = "SCD_REPL_STRATEGY_CHAIN_LOOKUP", schema = "SCD")
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
public class ReplenishmentStrategyChain {
    @Id
    @GenericGenerator(
        name = "replStrategyChainSequence",
        type = ExistingOrSequenceIdGenerator.class,
        parameters = {
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "SCD.SEQ_REPL_STRATEGY_CHAIN_LOOKUP"),
            @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
            @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),
            @org.hibernate.annotations.Parameter(name = "optimizer", value = "pooled-lo")
        }
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "replStrategyChainSequence")
    @ViewField(editable = false)
    @Column(name = "ID_REPL_STRATEGY_CHAIN")
    private Long id;
    @Column(name = "IS_KGT")
    private Boolean isKgt;
    @ManyToOne
    @JoinColumn(name = "ID_COLLECTION")
    private Collection collection;
    @ManyToOne
    @JoinColumn(name = "ID_SUBSEASON")
    private SubSeason subSeason;
    @OneToOne
    @JoinColumn(name = "ID_SC_LEVEL")
    @JoinColumn(name = "ID_SC_ST_LEVEL")
    private StyleColorStScaleLevel level;
}
