package ru.sportmaster.scd.entity.forecast;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import ru.sportmaster.scd.config.json.MfpCalendarDayDeserializer;
import ru.sportmaster.scd.config.json.MfpCalendarDaySerializer;
import ru.sportmaster.scd.entity.IEntity;
import ru.sportmaster.scd.entity.dictionary.Collection;
import ru.sportmaster.scd.entity.dictionary.TradeRegion;
import ru.sportmaster.scd.entity.pivot.DivisionTMA;
import ru.sportmaster.scd.persistence.ExistingOrSequenceIdGenerator;
import ru.sportmaster.scd.ui.view.annotation.View;
import ru.sportmaster.scd.ui.view.annotation.ViewField;
import ru.sportmaster.scd.ui.view.annotation.ViewRenderer;

@View
@Entity
@Table(name = "SCD_BUSINESS_REGION_PROMOACTIONS", schema = "SCD")
//Lombok
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
//Hibernate
@Cacheable
@Cache(region = "referenceCache", usage = CacheConcurrencyStrategy.READ_WRITE)
public class PromoAction implements IEntity<Long> {
    @Id
    @GenericGenerator(
        name = "promoActionSequence",
        type = ExistingOrSequenceIdGenerator.class,
        parameters = {
            @org.hibernate.annotations.Parameter(
                name = "sequence_name",
                value = "SCD.SEQ_SCD_BUSINESS_REGION_PROMOACTIONS"),
            @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
            @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),
            @org.hibernate.annotations.Parameter(name = "optimizer", value = "pooled-lo")
        }
    )
    @ViewField(order = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "promoActionSequence")
    @Column(name = "ID_PROMOACTION")
    private Long id;
    @ViewField(order = 2)
    @Column(name = "PROMOACTION_NAME")
    private String name;
    @OneToOne
    @JoinColumn(name = "ID_DIVISION_TMA")
    @View(flat = true)
    @ViewField(renderer = @ViewRenderer(srcField = "name"), order = 3)
    private DivisionTMA division;
    @ManyToOne
    @JoinColumn(name = "ID_TRADE_REGION")
    @View(flat = true)
    @ViewField(renderer = @ViewRenderer(srcField = "name"), order = 4)
    private TradeRegion region;
    @ManyToOne
    @JoinColumn(name = "ID_COLLECTION")
    private Collection collection;
    @JsonSerialize(using = MfpCalendarDaySerializer.class)
    @JsonDeserialize(using = MfpCalendarDayDeserializer.class)
    @ViewField(renderer = @ViewRenderer(updType = LocalDate.class))
    @Column(name = "ID_DAY_BEGIN")
    private Integer beginDay;
    @JsonSerialize(using = MfpCalendarDaySerializer.class)
    @JsonDeserialize(using = MfpCalendarDayDeserializer.class)
    @ViewField(renderer = @ViewRenderer(updType = LocalDate.class))
    @Column(name = "ID_DAY_END")
    private Integer endDay;
    @Column(name = "IS_DELETED", precision = 1)
    private Boolean isDeleted;
}
