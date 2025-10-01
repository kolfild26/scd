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
import ru.sportmaster.scd.entity.adjustment.Business;
import ru.sportmaster.scd.ui.view.annotation.View;
import ru.sportmaster.scd.ui.view.annotation.ViewField;
import ru.sportmaster.scd.ui.view.type.DataFetchType;

@View(fetchType = DataFetchType.FULL)
@Entity
@Table(name = "SCD_DIVISION_SCD", schema = "SCD")
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
public class DivisionSCD {
    @Id
    @Column(name = "ID_DIVISION_SCD", precision = 38, nullable = false, updatable = false)
    private Long id;
    @Column(name = "DIVISION_SCD_DESCRIPTION")
    private String description;
    @View(flat = true)
    @ViewField
    @ManyToOne
    @JoinTable(
        schema = "SCD",
        name = "SCD_BSNSS_TMA_DIVSN_SCD_LINKS",
        joinColumns = @JoinColumn(name = "ID_DIVISION_SCD"),
        inverseJoinColumns = @JoinColumn(name = "ID_BUSINESS_TMA")
    )
    private Business business;
}
