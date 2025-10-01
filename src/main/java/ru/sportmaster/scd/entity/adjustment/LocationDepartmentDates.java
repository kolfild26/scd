package ru.sportmaster.scd.entity.adjustment;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
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

@View
@Entity
@Table(name = "ASCD_V_LOCATION_DEPARTMENT_DATES", schema = "SCD_API")
//Lombok
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
//Hibernate
@Immutable
@Cacheable
@Cache(region = "referenceCache", usage = CacheConcurrencyStrategy.READ_ONLY)
public class LocationDepartmentDates {
    @Id
    @Column(name = "ID_DEPARTMENT", precision = 38, nullable = false, updatable = false)
    private Long id;
    @Column(name = "FACT_OPEN_DATE")
    @ViewField(order = 2, required = false)
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate factOpenDate;
    @Column(name = "FACT_CLOSE_DATE")
    @ViewField(order = 3, required = false)
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate factCloseDate;
    @Column(name = "MDM_PLAN_OPEN_DATE")
    @ViewField(required = false)
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate mdmPlanOpenDate;
    @Column(name = "MDM_PLAN_CLOSE_DATE")
    @ViewField(required = false)
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate mdmPlanCloseDate;
    @Column(name = "MFP_PLAN_OPEN_DATE")
    @ViewField(required = false)
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate mfpPlanOpenDate;
    @Column(name = "MFP_PLAN_CLOSE_DATE")
    @ViewField(required = false)
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate mfpPlanCloseDate;
}
