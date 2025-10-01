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
import org.hibernate.annotations.Where;
import ru.sportmaster.scd.ui.view.annotation.View;

@View
@Entity
@Table(name = "ASCD_V_RPAS_PROJECT_LOOKUP", schema = "SCD_API")
//Lombok
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
//Hibernate
@Cacheable
@Cache(region = "referenceCache", usage = CacheConcurrencyStrategy.READ_ONLY)
@Where(clause = "IS_ACTIVE = 1 AND IS_DELETED IS NULL")
public class PlProject {
    @Id
    @Column(name = "ID_PROJECT")
    private Long id;
    @Column(name = "MONIKER")
    private String name;
    @Column(name = "IS_ACTIVE")
    private Boolean isActive;
    @Column(name = "IS_DELETED")
    private Boolean isDeleted;
    @ManyToOne
    @JoinColumn(name = "ID_BUSINESS")
    private PlBusiness business;
}
