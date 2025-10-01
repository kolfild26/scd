package ru.sportmaster.scd.entity.simple3dplan;

import static java.util.Objects.isNull;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
import ru.sportmaster.scd.config.json.DistrSourceDeserializer;
import ru.sportmaster.scd.entity.IEntity;
import ru.sportmaster.scd.entity.adjustment.Business;
import ru.sportmaster.scd.entity.dictionary.Collection;
import ru.sportmaster.scd.persistence.DistrSourceAttributeConverter;
import ru.sportmaster.scd.ui.view.annotation.View;
import ru.sportmaster.scd.ui.view.annotation.ViewField;
import ru.sportmaster.scd.ui.view.annotation.ViewRenderer;
import ru.sportmaster.scd.utils.PersistenceUtils;

@View
@Entity
@Table(name = "SCD_INITIAL_DISTR_SOURCE", schema = "SCD")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InitialDistrSource implements IEntity<InitialDistrSourceKey> {
    @EmbeddedId
    private InitialDistrSourceKey id = new InitialDistrSourceKey();

    @ManyToOne
    @View(flat = true)
    @MapsId(value = "collectionId")
    @JoinColumn(name = "ID_COLLECTION")
    @ViewField(renderer = @ViewRenderer(srcField = "name"), editable = false)
    private Collection collection;

    @ManyToOne
    @MapsId(value = "businessId")
    @JoinColumn(name = "ID_BUSINESS_TMA")
    private Business business;

    @ViewField
    @Column(name = "DISTR_SOURCE")
    @Convert(converter = DistrSourceAttributeConverter.class)
    @JsonDeserialize(using = DistrSourceDeserializer.class)
    private DistrSource distrSource;

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
