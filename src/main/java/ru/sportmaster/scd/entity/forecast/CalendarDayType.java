package ru.sportmaster.scd.entity.forecast;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
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
@Table(name = "SCD_PRD_CLND_DAY_TYPE", schema = "SCD")
//Lombok
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
//Hibernate
@Cacheable
@Cache(region = "referenceCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Where(clause = "IS_ACTIVE = 1")
public class CalendarDayType implements IEntity<CalendarDayTypeKey> {
    @EmbeddedId
    private CalendarDayTypeKey id = new CalendarDayTypeKey();
    @ViewField
    @Column(name = "DAY_TYPE")
    private String type;
    @ViewField
    @Column(name = "DAY_WEIGHT", precision = 2)
    private Double weight;
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
    @MapsId(value = "businessId")
    @JoinColumn(name = "ID_BUSINESS_TMA")
    private Business business;
}
