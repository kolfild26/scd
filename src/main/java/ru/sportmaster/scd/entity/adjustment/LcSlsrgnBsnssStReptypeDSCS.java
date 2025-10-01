package ru.sportmaster.scd.entity.adjustment;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.sportmaster.scd.entity.IEntity;
import ru.sportmaster.scd.entity.dictionary.Collection;
import ru.sportmaster.scd.entity.pivot.CountryGroup;
import ru.sportmaster.scd.entity.pivot.DivisionTMA;
import ru.sportmaster.scd.service.adjustment.ui.AdjustmentDocRowEditor;
import ru.sportmaster.scd.ui.resolver.JpaViewResolver;
import ru.sportmaster.scd.ui.view.annotation.CustomView;
import ru.sportmaster.scd.ui.view.annotation.View;
import ru.sportmaster.scd.ui.view.annotation.ViewField;
import ru.sportmaster.scd.ui.view.annotation.ViewRenderer;
import ru.sportmaster.scd.ui.view.type.DataFetchType;

@CustomView(
    resolver = JpaViewResolver.class,
    editor = AdjustmentDocRowEditor.class,
    fetchType = DataFetchType.INFINITY
)
@Entity
@Table(name = "SCD_DC_V_DSCS_COLL_SALESRGN_DTMATS_PS_VOLUME", schema = "SCD")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//Hibernate
@Cacheable
@Cache(region = "referenceCache", usage = CacheConcurrencyStrategy.READ_WRITE)
public class LcSlsrgnBsnssStReptypeDSCS implements IEntity<Long> {
    @Id
    @ViewField(editable = false)
    @Column(name = "ID_DSCS_COLL_SALESRGN_DTMATS_PS_VOLUME")
    private Long id;

    @ManyToOne
    @ViewField(editable = false)
    @JoinColumn(name = "ID_DEV_STYLECOLOR_SIZE")
    @View(flat = true)
    private DevColorSize size;

    @ManyToOne
    @JoinColumn(name = "ID_COLLECTION")
    @View(flat = true)
    @ViewField(renderer = @ViewRenderer(srcField = "name"), editable = false)
    private Collection collection;

    @View(flat = true)
    @ViewField(editable = false)
    @ManyToOne
    @JoinColumn(name = "ID_SALES_REGION")
    private CountryGroup countryGroup;

    @ManyToOne
    @JoinColumn(name = "ID_DIVISION_TMA_SOURCE")
    @View(flat = true)
    @ViewField(renderer = @ViewRenderer(srcField = "name"), editable = false)
    protected DivisionTMA divisionSource;

    @ManyToOne
    @JoinColumn(name = "ID_DIVISION_TMA_TARGET")
    @View(flat = true)
    @ViewField(renderer = @ViewRenderer(srcField = "name"), editable = false)
    protected DivisionTMA divisionTarget;

    @Column(name = "REPLACE_TYPE")
    @ViewField(editable = false)
    private Long replaceType;

    @ManyToOne
    @ViewField(editable = false)
    @JoinColumn(name = "ID_CORRECTION_LIST")
    private AdjustmentDoc document;

    @Column(name = "PS_VOLUME")
    @ViewField(editable = false)
    private Double psVolume;
}
