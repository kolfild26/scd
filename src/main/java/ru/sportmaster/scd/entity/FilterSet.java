package ru.sportmaster.scd.entity;

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
import ru.sportmaster.scd.entity.adjustment.Business;
import ru.sportmaster.scd.persistence.ExistingOrSequenceIdGenerator;
import ru.sportmaster.scd.ui.view.annotation.View;

@View
@Entity
@Table(name = "ASCD_FILTER_SET", schema = "SCD_API")
//Lombok
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
//Hibernate
@Cacheable
@Cache(region = "referenceCache", usage = CacheConcurrencyStrategy.READ_WRITE)
public class FilterSet {
    @Id
    @GenericGenerator(
        name = "filterSetSequence",
        type = ExistingOrSequenceIdGenerator.class,
        parameters = {
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "SEQ_ASCD_FILTER_SET"),
            @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
            @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),
            @org.hibernate.annotations.Parameter(name = "optimizer", value = "pooled-lo")
        }
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "filterSetSequence")
    @Column(name = "ID_FILTER_SET")
    private Long id;
    @Column(name = "NAME_FILTER_SET")
    private String name;
    @Column(name = "TYPE_FILTER_SET")
    private String type;
    @ManyToOne
    @JoinColumn(name = "ID_BUSINESS_TMA")
    private Business business;
    @Column(name = "VALUE_FILTER_SET")
    private String value;
}
