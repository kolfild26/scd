package ru.sportmaster.scd.entity.dictionary;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
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
@Table(name = "SCD_RD_MFP2_HIERARCHY_GENERAL", schema = "SCD")
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
public class RDHierarchyGeneral {
    @EmbeddedId
    private RDHierarchyGeneralKey id = new RDHierarchyGeneralKey();

    @Column(name = "ID_PARENT")
    private Long parentId;

    @Column(name = "MONIKER")
    private String moniker;

    @Column(name = "NODE_KEY_TREE_NAME")
    protected String treeKey;

    @Column(name = "IS_DELETED", precision = 1)
    private Boolean isDeleted;
}
