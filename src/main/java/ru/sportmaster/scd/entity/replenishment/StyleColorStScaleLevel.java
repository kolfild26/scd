package ru.sportmaster.scd.entity.replenishment;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
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
import ru.sportmaster.scd.entity.IEntity;
import ru.sportmaster.scd.ui.view.annotation.View;
import ru.sportmaster.scd.ui.view.annotation.ViewField;

@View(recoverable = true)
@Entity
@Table(name = "SCD_SC_ST_LEVEL_SCALE", schema = "SCD")
//Lombok
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
//Hibernate
@Cacheable
@Cache(region = "referenceCache", usage = CacheConcurrencyStrategy.READ_WRITE)
public class StyleColorStScaleLevel implements IEntity<StyleColorStScaleLevelKey> {
    @EmbeddedId
    private StyleColorStScaleLevelKey id = new StyleColorStScaleLevelKey();
    @ViewField(editable = false)
    @Column(name = "K_LOW")
    private Integer low;
    @ViewField
    @Column(name = "K_UP")
    private Integer high;
    @ViewField
    @Column(name = "FLAG_UP")
    private Boolean flagHigh;
    @MapsId(value = "levelId")
    @ManyToOne
    @JoinColumn(name = "ID_SC_LEVEL")
    private StyleColorScaleLevel level;
    @MapsId(value = "stLevelId")
    @ManyToOne
    @JoinColumn(name = "ID_SC_ST_LEVEL")
    private StyleColorStScaleLevelLookup stLevel;
    @Column(name = "IS_DELETED")
    private Boolean isDeleted;
}
