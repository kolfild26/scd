package ru.sportmaster.scd.entity.adjustment;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import ru.sportmaster.scd.entity.IEntity;
import ru.sportmaster.scd.ui.view.annotation.View;
import ru.sportmaster.scd.ui.view.annotation.ViewField;
import ru.sportmaster.scd.ui.view.annotation.ViewRenderer;
import ru.sportmaster.scd.utils.PersistenceUtils;

@View
@Entity
@Table(name = "SCD_MDM_DEV_STYLECOLOR_SIZE", schema = "SCD")
//Lombok
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
//Hibernate
@Immutable
@Cacheable
@Cache(region = "referenceCache", usage = CacheConcurrencyStrategy.READ_WRITE)
public class DevColorSize implements IEntity<Long> {
    @Id
    @ViewField(renderer = @ViewRenderer(updField = "size", updType = DevColorSize.class))
    @Column(name = "ID_DEV_STYLECOLOR_SIZE", precision = 38, nullable = false, updatable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "ID_DEV_STYLECOLOR")
    @View(flat = true)
    @ViewField(renderer = @ViewRenderer(srcField = "name"), editable = false)
    private DevColor color;
    @ViewField(editable = false)
    @Column(name = "DEV_SIZE")
    private String name;
    @Column(name = "IS_DELETED", precision = 1)
    private Boolean isDeleted;

    @Override
    @SuppressWarnings("all")
    public boolean equals(Object o) {
        return PersistenceUtils.equals(this, o);
    }

    @Override
    public int hashCode() {
        return PersistenceUtils.hashCode(this);
    }
}
