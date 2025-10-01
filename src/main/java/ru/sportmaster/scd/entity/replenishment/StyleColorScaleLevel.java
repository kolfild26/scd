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
@Table(name = "SCD_SC_LEVEL_SCALE", schema = "SCD")
//Lombok
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
//Hibernate
@Cacheable
@Cache(region = "referenceCache", usage = CacheConcurrencyStrategy.READ_WRITE)
public class StyleColorScaleLevel implements IEntity<Long> {
    @Id
    @GenericGenerator(
        name = "styleColorScaleLevelSequence",
        type = ExistingOrSequenceIdGenerator.class,
        parameters = {
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "SCD.SEQ_SC_LEVEL_SCALE"),
            @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
            @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),
            @org.hibernate.annotations.Parameter(name = "optimizer", value = "pooled-lo")
        }
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "styleColorScaleLevelSequence")
    @Column(name = "ID_SC_LEVEL")
    private Long id;
    @ViewField(editable = false)
    @Column(name = "ORDER_")
    private Long order;
    @ViewField
    @Column(name = "STYLECOLOR_LEVEL")
    private String level;
    @ViewField(editable = false)
    @Column(name = "K_SC_LOW")
    private Integer low;
    @ViewField
    @Column(name = "K_SC_HIGH")
    private Integer high;
    @ViewField
    @Column(name = "FLAG_SC_HIGH")
    private Boolean flagHigh;
    @ViewField
    @Column(name = "STYLECOLOR_DESCRIPTION")
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
