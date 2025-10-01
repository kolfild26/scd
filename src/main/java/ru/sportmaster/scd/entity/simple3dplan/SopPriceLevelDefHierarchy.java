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
import ru.sportmaster.scd.ui.view.annotation.ViewField;
import ru.sportmaster.scd.ui.view.type.DataFetchType;
import ru.sportmaster.scd.utils.PersistenceUtils;

@View(fetchType = DataFetchType.FULL)
@Entity
@Table(name = "ASCD_V_HIERARCHY_ONLY_SOP_HIER_SM", schema = "SCD_API")
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
public class SopPriceLevelDefHierarchy implements IEntity<Long> {
    @Id
    @Column(name = "ID_PLANNING_STRING", precision = 14, nullable = false, updatable = false)
    private Long id;

    @ViewField(order = 4, editable = false)
    @Column(name = "WOSM_PLANNINGSTRING")
    private String planningRow;

    @ViewField(order = 3, editable = false)
    @Column(name = "WOSM_SUBCATEGORY")
    private String subCategory;

    @ViewField(order = 2, editable = false)
    @Column(name = "WOSM_CATEGORY")
    private String category;

    @ViewField(order = 1, editable = false)
    @Column(name = "WOSM_WAREGROUP")
    private String wareGroup;

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
