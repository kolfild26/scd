package ru.sportmaster.scd.entity.replenishment;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "ASCD_V_KGT_ATTRIBUTE", schema = "SCD_API")
//Lombok
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
//Hibernate
@Cacheable
@Cache(region = "referenceCache", usage = CacheConcurrencyStrategy.READ_ONLY)
public class KgtSettingsAttrValue {
    @Id
    @Column(name = "ID_NODE")
    private Long id;
    @Column(name = "NODE_KEY_TREE_NAME")
    private String treeKey;
    @Column(name = "ID_GRP_OF_WARE")
    private Long wareGroupId;
    @Column(name = "ID_CATEGORY")
    private Long categoryId;
    @Column(name = "ID_SUBCATEGORY")
    private Long subCategoryId;
    @Column(name = "ID_GRP")
    private Long wareClassId;
}
