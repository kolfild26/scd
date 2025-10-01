package ru.sportmaster.scd.entity.dictionary;

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
import ru.sportmaster.scd.ui.view.annotation.View;
import ru.sportmaster.scd.ui.view.type.DataFetchType;

@View(fetchType = DataFetchType.FULL)
@Entity
@Table(name = "SCD_RPAS_RD_OS_HIERARCHY", schema = "SCD")
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
public class ShopOSHierarchy implements IShopHierarchy {
    @Id
    @Column(name = "ID_NODE", precision = 38, nullable = false, updatable = false)
    private Long id;
    @Column(name = "ID_PARENT")
    private Long parentId;
    @Column(name = "MONIKER")
    private String moniker;
    @Column(name = "LEVEL_NUM")
    private String level;
    @Column(name = "NODE_KEY_TREE_NAME")
    protected String treeKey;
    @Column(name = "IS_DELETED", precision = 1)
    private Boolean isDeleted;
}
