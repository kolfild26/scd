package ru.sportmaster.scd.entity.dictionary;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import ru.sportmaster.scd.ui.view.annotation.View;
import ru.sportmaster.scd.ui.view.annotation.ViewField;
import ru.sportmaster.scd.ui.view.type.DataFetchType;

@View(fetchType = DataFetchType.FULL)
@Entity
@Table(name = "SCD_PARTITION_DIV_TMA", schema = "SCD")
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
public class Partition {
    @Id
    @Column(name = "ID_PARTITION_DIV_TMA", precision = 38, nullable = false, updatable = false)
    private Long id;
    @Column(name = "PARTITION_DESCRIPTION")
    private String description;
    @View(flat = true)
    @ViewField
    @ManyToOne
    @JoinTable(
        schema = "SCD",
        name = "SCD_DIVSN_SCD_PARTITION_LINKS",
        joinColumns = @JoinColumn(name = "ID_PARTITION_DIV_TMA"),
        inverseJoinColumns = @JoinColumn(name = "ID_DIVISION_SCD")
    )
    private DivisionSCD division;
}
