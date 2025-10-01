package ru.sportmaster.scd.entity.planlimit;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import ru.sportmaster.scd.ui.view.annotation.View;

@View
@Entity
@Table(name = "ASCD_V_RPAS_FLATTED_HIERARCHY", schema = "SCD_API")
//Lombok
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
//Hibernate
@Cacheable
@Cache(region = "referenceCache", usage = CacheConcurrencyStrategy.READ_ONLY)
public class PlTreeData {
    @Id
    @Column(name = "ID_DEPARTMENT", nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "ID_PROJECT")
    private PlProject project;
    @ManyToOne
    @JoinColumn(name = "ID_CITY")
    private PlCity city;
}
