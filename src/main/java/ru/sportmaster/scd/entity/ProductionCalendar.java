package ru.sportmaster.scd.entity;

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
import ru.sportmaster.scd.ui.view.annotation.View;

@View
@Entity
@Table(name = "ASCD_V_PRODUCTION_CALENDAR", schema = "SCD_API")
//Lombok
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
//Hibernate
@Cacheable
@Cache(region = "referenceCache", usage = CacheConcurrencyStrategy.READ_ONLY)
public class ProductionCalendar {
    @Id
    @Column(name = "ID_DAY")
    private Long id;
    @Column(name = "DAY")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate day;
    @Column(name = "ID_DAY_TYPE")
    private String typeId;
    @Column(name = "DAY_TYPE")
    private String type;
    @Column(name = "YEAR")
    private Integer year;
    @Column(name = "SUB_SEASON")
    private String subSeason;
    @Column(name = "COLLECTION")
    private String collection;
}
