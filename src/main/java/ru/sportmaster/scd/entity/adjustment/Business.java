package ru.sportmaster.scd.entity.adjustment;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import ru.sportmaster.scd.entity.IEntity;
import ru.sportmaster.scd.ui.view.annotation.View;
import ru.sportmaster.scd.ui.view.annotation.ViewField;

@View
@Entity
@Table(name = "SCD_MDM_BUSINESS_TMA", schema = "SCD")
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
public class Business implements IEntity<Long> {
    @Id
    @Column(name = "ID_BUSINESS_TMA", precision = 38, nullable = false, updatable = false)
    private Long id;
    @ViewField(order = 3)
    @Column(name = "BUSINESS_TMA_RUS")
    private String name;
    @Column(name = "BUSINESS_TMA_ENG")
    private String nameEng;
    @Column(name = "IS_DELETED", precision = 1)
    private Boolean isDeleted;
}
