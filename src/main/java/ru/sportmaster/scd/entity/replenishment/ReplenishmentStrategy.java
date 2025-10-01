package ru.sportmaster.scd.entity.replenishment;

import static java.util.Objects.isNull;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
import ru.sportmaster.scd.entity.pivot.DivisionTMA;
import ru.sportmaster.scd.persistence.ExistingOrSequenceIdGenerator;
import ru.sportmaster.scd.ui.view.annotation.View;
import ru.sportmaster.scd.ui.view.annotation.ViewField;
import ru.sportmaster.scd.utils.PersistenceUtils;

@View(recoverable = true)
@Entity
@Table(name = "SCD_REPL_STRATEGY_LOOKUP", schema = "SCD")
//Lombok
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
//Hibernate
@Cacheable
@Cache(region = "referenceCache", usage = CacheConcurrencyStrategy.READ_WRITE)
public class ReplenishmentStrategy implements IEntity<Long> {
    @Id
    @GenericGenerator(
        name = "replenishmentStrategySequence",
        type = ExistingOrSequenceIdGenerator.class,
        parameters = {
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "SCD.SEQ_REPL_STRATEGY_LOOKUP"),
            @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
            @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),
            @org.hibernate.annotations.Parameter(name = "optimizer", value = "pooled-lo")
        }
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "replenishmentStrategySequence")
    @Column(name = "ID_REPL_STRATEGY")
    private Long id;
    @ViewField(editable = false)
    @Column(name = "REPL_ORDER")
    private Long order;
    @ViewField
    @Column(name = "REPL_STRATEGY")
    private String name;
    @ViewField
    @Column(name = "REPL_DESCRIPTION")
    private String description;
    @ManyToOne
    @JoinColumn(name = "ID_BUSINESS_TMA")
    private Business business;
    @ManyToOne
    @JoinColumn(name = "ID_DIVISION_TMA")
    private DivisionTMA division;
    @Column(name = "IS_DELETED")
    private Boolean isDeleted;

    @Override
    @SuppressWarnings("all")
    public boolean equals(Object o) {
        return PersistenceUtils.equals(this, o);
    }

    @Override
    public int hashCode() {
        return isNull(getId()) ? 0 : getId().hashCode();
    }
}
