package ru.sportmaster.scd.entity.returnlogistic;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;
import ru.sportmaster.scd.config.json.WareAccordanceLevelDeserializer;
import ru.sportmaster.scd.entity.adjustment.Business;
import ru.sportmaster.scd.entity.dictionary.Collection;
import ru.sportmaster.scd.entity.pivot.CountryGroup;
import ru.sportmaster.scd.persistence.WareAccordanceLevelAttributeConverter;
import ru.sportmaster.scd.ui.view.annotation.View;
import ru.sportmaster.scd.ui.view.annotation.ViewField;
import ru.sportmaster.scd.ui.view.annotation.ViewRenderer;

@View
@Entity
@Table(name = "SCD_RL_RETURN_TYPE", schema = "SCD")
//Lombok
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
//Hibernate
@Cacheable
@Cache(region = "referenceCache", usage = CacheConcurrencyStrategy.READ_WRITE)
public class AccordanceTable {
    @Id
    @Column(name = "ID_RETURN_TYPE")
    private Long id;

    @ManyToOne
    @MapsId(value = "businessId")
    @JoinColumn(name = "ID_BUSINESS_TMA")
    private Business business;

    @ManyToOne
    @JoinColumn(name = "ID_COLLECTION")
    @View(flat = true)
    @ViewField(renderer = @ViewRenderer(srcField = "name"))
    private Collection toCollection;

    @ViewField(editable = false)
    @Column(name = "WOSM_PLANNINGSTRING")
    private String toPlanningLine;

    @ManyToOne
    @MapsId(value = "countryGroupId")
    @JoinColumn(name = "ID_COUNTRY_GROUP")
    private CountryGroup toCountryGroup;

    @ManyToOne
    @MapsId(value = "countryGroupId")
    @JoinColumn(name = "ID_COUNTRY_GROUP")
    private CountryGroup fromCountryGroup;

    @ViewField
    @Column(name = "WARE_ACCORDANCE_LEVEL")
    @Convert(converter = WareAccordanceLevelAttributeConverter.class)
    @JsonDeserialize(using = WareAccordanceLevelDeserializer.class)
    private WareAccordanceLevel wareAccordanceLevel;
}
