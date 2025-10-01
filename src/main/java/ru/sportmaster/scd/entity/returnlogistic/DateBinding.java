package ru.sportmaster.scd.entity.returnlogistic;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.sportmaster.scd.config.json.DayDeserializer;
import ru.sportmaster.scd.config.json.DaySerializer;
import ru.sportmaster.scd.entity.adjustment.Business;
import ru.sportmaster.scd.entity.dictionary.Day;
import ru.sportmaster.scd.entity.replenishment.SubSeason;
import ru.sportmaster.scd.ui.view.annotation.View;
import ru.sportmaster.scd.ui.view.annotation.ViewField;
import ru.sportmaster.scd.ui.view.annotation.ViewRenderer;

@View
@Entity
@Table(name = "SCD_RL_SEASON_RETURN_STGS", schema = "SCD")
//Lombok
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
//Hibernate
@Cacheable
@Cache(region = "referenceCache", usage = CacheConcurrencyStrategy.READ_WRITE)
public class DateBinding {
    @Id
    @Column(name = "ID_DATE_BINDING")
    private Long id;

    @ManyToOne
    @MapsId(value = "businessId")
    @JoinColumn(name = "ID_BUSINESS_TMA")
    private Business business;

    @ManyToOne
    @MapsId(value = "subSeasonId")
    @JoinColumn(name = "SEASON_CODE")
    @ViewField(order = 0, editable = false)
    private SubSeason subSeason;

    @ManyToOne
    @JoinColumn(name = "ID_DAY_DATE_FROM")
    @JsonSerialize(using = DaySerializer.class)
    @JsonDeserialize(using = DayDeserializer.class)
    @ViewField(
            order = 1,
            renderer = @ViewRenderer(
                    srcField = "day",
                    srcType = LocalDate.class,
                    updType = LocalDate.class
            )
    )
    private Day beginDate;

    @ManyToOne
    @JoinColumn(name = "ID_DAY_DATE_TO")
    @JsonSerialize(using = DaySerializer.class)
    @JsonDeserialize(using = DayDeserializer.class)
    @ViewField(
            order = 2,
            renderer = @ViewRenderer(
                    srcField = "day",
                    srcType = LocalDate.class,
                    updType = LocalDate.class
            ))
    private Day endDate;
}
