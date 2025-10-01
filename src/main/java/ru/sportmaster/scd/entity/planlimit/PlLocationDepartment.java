package ru.sportmaster.scd.entity.planlimit;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import ru.sportmaster.scd.entity.pivot.DivisionTMA;
import ru.sportmaster.scd.ui.view.annotation.View;
import ru.sportmaster.scd.ui.view.annotation.ViewField;

@View
@Entity
@Table(name = "SCD_MDM_LOCATION_DEPARTMENT", schema = "SCD")
//Lombok
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
//Hibernate
@Cacheable
@Cache(region = "referenceCache", usage = CacheConcurrencyStrategy.READ_ONLY)
public class PlLocationDepartment {
    @Id
    @Column(name = "ID_DEPARTMENT", precision = 38, nullable = false, updatable = false)
    private Long id;
    @ViewField
    @Column(name = "ID_OBJ")
    private Integer code;
    @ViewField
    @Column(name = "MONIKER_RUS")
    private String moniker;
    @ViewField
    @Column(name = "NAME")
    private String name;
    @ManyToOne
    @JoinColumn(name = "ID_DIVISION_TMA")
    private DivisionTMA division;
    @OneToOne
    @JoinColumn(name = "ID_DEPARTMENT")
    private PlTreeData tree;
}
