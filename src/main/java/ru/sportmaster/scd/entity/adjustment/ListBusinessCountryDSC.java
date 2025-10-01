package ru.sportmaster.scd.entity.adjustment;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.sportmaster.scd.entity.dictionary.Collection;
import ru.sportmaster.scd.entity.pivot.Country;
import ru.sportmaster.scd.entity.pivot.DivisionTMA;
import ru.sportmaster.scd.ui.view.annotation.View;
import ru.sportmaster.scd.ui.view.annotation.ViewField;
import ru.sportmaster.scd.ui.view.annotation.ViewRenderer;

@MappedSuperclass
//Lombok
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ListBusinessCountryDSC {
    @ManyToOne
    @JoinColumn(name = "ID_COLLECTION")
    @View(flat = true)
    @ViewField(renderer = @ViewRenderer(srcField = "name"))
    protected Collection collection;
    @ManyToOne
    @JoinColumn(name = "ID_DEV_STYLECOLOR")
    @View(flat = true)
    @ViewField(renderer = @ViewRenderer(srcField = "id"))
    protected DevColorModel model;
    @ManyToOne
    @JoinColumn(name = "ID_COUNTRY")
    @View(flat = true)
    @ViewField(renderer = @ViewRenderer(srcField = "name"))
    protected Country country;
    @ManyToOne
    @JoinColumn(name = "ID_DIVISION_TMA")
    @View(flat = true)
    @ViewField(renderer = @ViewRenderer(srcField = "name"))
    protected DivisionTMA division;
    @ViewField(required = false)
    @Column(name = "STOP_LIST", precision = 1)
    private Boolean stlType;
    @ManyToOne
    @JoinColumn(name = "ID_CORRECTION_LIST")
    protected AdjustmentDoc document;
}
