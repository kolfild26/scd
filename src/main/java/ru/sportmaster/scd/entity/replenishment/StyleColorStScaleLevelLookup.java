package ru.sportmaster.scd.entity.replenishment;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
import ru.sportmaster.scd.persistence.ExistingOrSequenceIdGenerator;
import ru.sportmaster.scd.ui.view.annotation.View;

@View(recoverable = true)
@Entity
@Table(name = "SCD_SC_ST_LEVEL_LOOKUP", schema = "SCD")
//Lombok
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
//Hibernate
@Cacheable
@Cache(region = "referenceCache", usage = CacheConcurrencyStrategy.READ_WRITE)
public class StyleColorStScaleLevelLookup implements IEntity<Long> {
    @Id
    @GenericGenerator(
        name = "styleColorStScaleLevelSequence",
        type = ExistingOrSequenceIdGenerator.class,
        parameters = {
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "SCD.SEQ_SC_ST_LEVEL_LOOKUP"),
            @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
            @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),
            @org.hibernate.annotations.Parameter(name = "optimizer", value = "pooled-lo")
        }
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "styleColorStScaleLevelSequence")
    @Column(name = "ID_SC_ST_LEVEL")
    private Long id;
    @Column(name = "STYLECOLOR_STORE_LEVEL")
    private String level;
    @Column(name = "ORDER_")
    private Integer order;
    @Column(name = "IS_DELETED")
    private Boolean isDeleted;
}
