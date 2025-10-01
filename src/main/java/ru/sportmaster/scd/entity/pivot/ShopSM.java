package ru.sportmaster.scd.entity.pivot;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
import ru.sportmaster.scd.utils.PersistenceUtils;

@View
@Entity
@Table(name = "SCD_RPAS_RD_SM_HIERARCHY", schema = "SCD")
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
public class ShopSM implements IEntity<Long> {
    @Id
    @Column(name = "ID_NODE", precision = 38, nullable = false, updatable = false)
    private Long id;
    @Column(name = "MONIKER")
    private String moniker;
    @Column(name = "NODE_KEY_TREE_NAME")
    private String treeKey;
    @Column(name = "LEVEL_NUM")
    private String level;
    @OneToOne
    @ViewField
    @View(flat = true)
    @JoinColumn(name = "ID_NODE")
    private LocationDepartment department;
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
