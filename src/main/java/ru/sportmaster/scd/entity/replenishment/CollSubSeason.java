package ru.sportmaster.scd.entity.replenishment;

import static java.util.Objects.isNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.sportmaster.scd.config.json.SubSeasonDeserializer;
import ru.sportmaster.scd.entity.IEntity;
import ru.sportmaster.scd.entity.adjustment.Business;
import ru.sportmaster.scd.entity.dictionary.Collection;
import ru.sportmaster.scd.entity.pivot.DivisionTMA;
import ru.sportmaster.scd.service.replenishment.CollSubSeasonEditor;
import ru.sportmaster.scd.ui.resolver.JpaViewResolver;
import ru.sportmaster.scd.ui.view.annotation.CustomView;
import ru.sportmaster.scd.ui.view.annotation.View;
import ru.sportmaster.scd.ui.view.annotation.ViewField;
import ru.sportmaster.scd.ui.view.annotation.ViewRenderer;

@CustomView(
    recoverable = true,
    resolver = JpaViewResolver.class,
    editor = CollSubSeasonEditor.class
)
@Entity
@Table(name = "SCD_COLL_SUBSEASON_LINK", schema = "SCD")
//Lombok
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
//Hibernate
@Cacheable
@Cache(region = "referenceCache", usage = CacheConcurrencyStrategy.READ_WRITE)
public class CollSubSeason implements IEntity<CollSubSeasonKey> {
    @EmbeddedId
    private CollSubSeasonKey id = new CollSubSeasonKey();
    @ViewField(editable = false, order = 1)
    @Column(name = "ORDER_")
    private Long order;
    @View(flat = true)
    @ViewField(editable = false, order = 2, renderer = @ViewRenderer(srcField = "name"))
    @MapsId("businessId")
    @ManyToOne
    @JoinColumn(name = "ID_BUSINESS_TMA")
    private Business business;
    @View(flat = true)
    @ViewField(editable = false, renderer = @ViewRenderer(srcField = "name"))
    @MapsId("collectionId")
    @ManyToOne
    @JoinColumn(name = "ID_COLLECTION")
    private Collection collection;
    @View(flat = true)
    @MapsId("subSeasonId")
    @ManyToOne
    @JsonDeserialize(using = SubSeasonDeserializer.class)
    @ViewField(renderer = @ViewRenderer(srcField = "name", updField = "name", updType = String.class))
    @JoinColumn(name = "ID_SUBSEASON")
    private SubSeason subSeason;
    @MapsId("divisionId")
    @ManyToOne
    @View(flat = true)
    @ViewField(editable = false, renderer = @ViewRenderer(srcField = "name"))
    @JoinColumn(name = "ID_DIVISION_TMA")
    private DivisionTMA division;
    @ViewField(editable = false)
    @Column(name = "DATE_BEGIN")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate beginDate;
    @ViewField
    @Column(name = "DATE_END")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate endDate;
    @ViewField
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "IS_DELETED")
    private Boolean isDeleted;

    @PrePersist
    private void prePersist() {
        if (isNull(id)) {
            setId(CollSubSeasonKey.builder()
                .collectionId(getCollection().getId())
                .businessId(getBusiness().getId())
                .divisionId(getDivision().getId())
                .subSeasonId(getSubSeason().getId())
                .build());
        }
    }
}
