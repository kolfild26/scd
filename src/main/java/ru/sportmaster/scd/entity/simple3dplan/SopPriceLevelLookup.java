package ru.sportmaster.scd.entity.simple3dplan;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
import ru.sportmaster.scd.ui.view.type.DataFetchType;
import ru.sportmaster.scd.utils.PersistenceUtils;

@View(fetchType = DataFetchType.FULL)
@Entity
@Table(name = "SCD_SOP_PRICE_LEVEL_LOOKUP", schema = "SCD")
//Lombok
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
//Hibernate
@Immutable
@Cacheable
@Cache(region = "referenceCache", usage = CacheConcurrencyStrategy.READ_ONLY)
public class SopPriceLevelLookup implements IEntity<Long> {
    @Id
    @Column(name = "ID_PRICE_LEVEL", precision = 14, nullable = false, updatable = false)
    private Long id;

    @Column(name = "PRICE_LEVEL", length = 256)
    private String name;

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
