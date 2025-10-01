package ru.sportmaster.scd.entity.simple3dplan;

import static java.util.Objects.isNull;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.sportmaster.scd.entity.IEntity;
import ru.sportmaster.scd.entity.dictionary.Collection;
import ru.sportmaster.scd.entity.dictionary.Partition;
import ru.sportmaster.scd.entity.pivot.CountryGroup;
import ru.sportmaster.scd.ui.view.annotation.View;
import ru.sportmaster.scd.ui.view.annotation.ViewField;
import ru.sportmaster.scd.ui.view.annotation.ViewFileUpdate;
import ru.sportmaster.scd.ui.view.annotation.ViewRenderer;
import ru.sportmaster.scd.utils.PersistenceUtils;

@View(
    fileUpdate = @ViewFileUpdate(updatable = true)
)
@Entity
@Table(name = "SCD_SOP_PRICE_LEVEL_DEF", schema = "SCD")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SopPriceLevelDef implements IEntity<SopPriceLevelDefKey> {
    @EmbeddedId
    private SopPriceLevelDefKey id = new SopPriceLevelDefKey();

    @View(flat = true)
    @ManyToOne
    @MapsId(value = "nodeId")
    @JoinColumn(name = "ID_NODE")
    private SopPriceLevelDefHierarchy planningRowHierarchy;

    @ManyToOne
    @MapsId(value = "countryGroupId")
    @JoinColumn(name = "ID_COUNTRY_GROUP")
    private CountryGroup countryGroup;

    @ManyToOne
    @MapsId(value = "collectionId")
    @JoinColumn(name = "ID_COLLECTION")
    private Collection collection;

    @ManyToOne
    @MapsId(value = "partitionId")
    @JoinColumn(name = "ID_PARTITION_DIV_TMA")
    private Partition partition;

    @ManyToOne
    @View(flat = true)
    @MapsId(value = "priceLevelId")
    @JoinColumn(name = "ID_PRICE_LEVEL")
    @ViewField(renderer = @ViewRenderer(srcField = "name"), editable = false)
    private SopPriceLevelLookup priceLevel;

    @ViewField(editable = false)
    @Column(name = "PRICE_MIN")
    private Long leftBorder;

    @ViewField
    @Column(name = "PRICE_MAX")
    private Long rightBorder;

    @Override
    @SuppressWarnings("all")
    public boolean equals(Object o) {
        return PersistenceUtils.equals(this, o);
    }

    @Override
    public int hashCode() {
        return isNull(id) ? 0 : id.hashCode();
    }
}
