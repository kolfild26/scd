package ru.sportmaster.scd.entity.planlimit;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import ru.sportmaster.scd.entity.dictionary.Collection;
import ru.sportmaster.scd.entity.dictionary.DivisionSCD;
import ru.sportmaster.scd.entity.pivot.LocationDepartment;
import ru.sportmaster.scd.persistence.ExistingOrSequenceIdGenerator;
import ru.sportmaster.scd.persistence.ListLongConverter;
import ru.sportmaster.scd.ui.view.annotation.View;
import ru.sportmaster.scd.ui.view.annotation.ViewField;
import ru.sportmaster.scd.ui.view.annotation.ViewRenderer;

@View
@Entity
@Table(name = "ASCD_PL_PRIORITY_MATRIX", schema = "SCD_API")
//Lombok
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
//Hibernate
@Cacheable
@Cache(region = "referenceCache", usage = CacheConcurrencyStrategy.READ_WRITE)
public class PlPriorityMatrix {
    @Id
    @GenericGenerator(
        name = "priorityMatrixSequence",
        type = ExistingOrSequenceIdGenerator.class,
        parameters = {
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "SEQ_ASCD_PL_PRIORITY_MATRIX"),
            @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
            @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),
            @org.hibernate.annotations.Parameter(name = "optimizer", value = "pooled-lo")
        }
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "priorityMatrixSequence")
    @Column(name = "ID_PRIORITY_MATRIX")
    private Long id;
    @ViewField(order = 0)
    @Column(name = "PRIORITY")
    private Integer priority;
    @View(flat = true)
    @ViewField(renderer = @ViewRenderer(srcField = "name"), order = 1)
    @ManyToOne
    @JoinColumn(name = "ID_PROJECT")
    private PlProject project;
    @View(flat = true)
    @ViewField(renderer = @ViewRenderer(srcField = "name"), order = 2)
    @ManyToOne
    @JoinColumn(name = "ID_CITY")
    private PlGroupedCity city;
    @View(flat = true)
    @ViewField(renderer = @ViewRenderer(srcField = "name"), order = 3)
    @ManyToOne
    @JoinColumn(name = "ID_DEPARTMENT")
    private LocationDepartment department;
    @ManyToOne
    @JoinColumn(name = "ID_DIVISION_SCD")
    private DivisionSCD division;
    @ManyToOne
    @JoinColumn(name = "ID_COLLECTION")
    private Collection collection;
    @Column(name = "ID_SHOPS")
    @Convert(converter = ListLongConverter.class)
    private List<Long> shopIds;
}
