package ru.sportmaster.scd.entity.adjustment;

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
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.sportmaster.scd.entity.IEntity;
import ru.sportmaster.scd.service.adjustment.ui.AdjustmentDocRowEditor;
import ru.sportmaster.scd.ui.resolver.JpaViewResolver;
import ru.sportmaster.scd.ui.view.annotation.CustomView;
import ru.sportmaster.scd.ui.view.annotation.ViewField;
import ru.sportmaster.scd.ui.view.type.DataFetchType;
import ru.sportmaster.scd.utils.PersistenceUtils;

@CustomView(
    resolver = JpaViewResolver.class,
    editor = AdjustmentDocRowEditor.class,
    fetchType = DataFetchType.INFINITY
)
@Entity
@Table(name = "SCD_DC_V_COLL_DSCS_SHOP_STL", schema = "SCD")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
//Hibernate
@Cacheable
@Cache(region = "referenceCache", usage = CacheConcurrencyStrategy.READ_WRITE)
public class StopListStoreDSCS extends ListStoreDSCS implements IEntity<Long> {
    @Id
    @Column(name = "ID_COLL_DSCS_SHOP_STL")
    private Long id;
    @ViewField(order = 30, required = false)
    @Column(name = "STOP_LIST", precision = 1)
    private Boolean stlType;

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
