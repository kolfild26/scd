package ru.sportmaster.scd.entity.forecast;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import ru.sportmaster.scd.entity.IEntity;
import ru.sportmaster.scd.entity.adjustment.Business;
import ru.sportmaster.scd.ui.view.annotation.View;
import ru.sportmaster.scd.ui.view.annotation.ViewField;

@View
@Entity
@Table(name = "SCD_COUNT_FACT_SALES_WEEKS", schema = "SCD")
//Lombok
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
//Hibernate
@Cacheable
@Cache(region = "referenceCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Where(clause = "IS_ACTIVE = 1")
public class FactSalesWeek implements IEntity<Long> {
    @Id
    @Column(name = "ID_COUNT_FACT_SALES_WEEKS")
    private Long id;
    @ViewField
    @Column(name = "COUNT_FACT_SALES_WEEKS", precision = 2)
    private Double value;
    @Column(name = "IS_ACTIVE", precision = 1)
    private Boolean isActive = true;
    @JsonIgnore
    @CreationTimestamp
    @Column(name = "DATE_CREATE")
    private LocalDateTime created;
    @JsonIgnore
    @UpdateTimestamp
    @Column(name = "DATE_MODIFY")
    private LocalDateTime updated;
    @ManyToOne
    @JoinColumn(name = "ID_BUSINESS_TMA")
    private Business business;
}
