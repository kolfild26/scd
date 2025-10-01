package ru.sportmaster.scd.entity.allocation;

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
import ru.sportmaster.scd.entity.dictionary.CategoryHierarchy;
import ru.sportmaster.scd.entity.dictionary.Collection;
import ru.sportmaster.scd.entity.pivot.LocationDepartment;
import ru.sportmaster.scd.persistence.ExistingOrSequenceIdGenerator;
import ru.sportmaster.scd.ui.view.annotation.View;
import ru.sportmaster.scd.ui.view.annotation.ViewField;

@SuppressWarnings("CPD-START")
@View
@Entity
@Table(name = "ASCD_SS_ALLOCATION_CLUSTER", schema = "SCD_API")
//Lombok
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
//Hibernate
@Cacheable
@Cache(region = "referenceCache", usage = CacheConcurrencyStrategy.READ_WRITE)
public class SafetyStockInAllocationCluster implements IEntity<Long> {
    @Id
    @GenericGenerator(
        name = "allocationClusterSequence",
        type = ExistingOrSequenceIdGenerator.class,
        parameters = {
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "SEQ_ASCD_SS_ALLOCATION_CLUSTER"),
            @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
            @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),
            @org.hibernate.annotations.Parameter(name = "optimizer", value = "pooled-lo")
        }
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "allocationClusterSequence")
    @Column(name = "ID_ALLOCATION_CLUST")
    private Long id;
    @ViewField
    @Column(name = "PERCENT_SS", precision = 4, scale = 3)
    private Double safetyStockPercent;
    @ViewField
    @Column(name = "MIN_W_SS", precision = 4)
    private Integer minWeekSafetyStock;
    @ViewField
    @Column(name = "MAX_W_SS", precision = 4)
    private Integer maxWeekSafetyStock;
    @ViewField
    @Column(name = "PERIOD_W_SS", precision = 4)
    private Integer periodWeekSafetyStock;
    @ViewField
    @ManyToOne
    @JoinColumn(name = "ID_DEPARTMENT")
    private LocationDepartment department;
    @ManyToOne
    @JoinColumn(name = "ID_COLLECTION")
    private Collection collection;
    @ManyToOne
    @JoinColumn(name = "ID_CATEGORY")
    private CategoryHierarchy category;
    @ManyToOne
    @JoinColumn(name = "ID_BUSINESS")
    private Business business;
}
