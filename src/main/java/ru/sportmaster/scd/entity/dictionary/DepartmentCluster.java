package ru.sportmaster.scd.entity.dictionary;

import static java.util.Objects.isNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import ru.sportmaster.scd.entity.adjustment.Business;
import ru.sportmaster.scd.ui.view.annotation.View;
import ru.sportmaster.scd.ui.view.annotation.ViewField;
import ru.sportmaster.scd.utils.PersistenceUtils;

@View
@Entity
@Table(name = "ASCD_V_DEPARTMENT_CLUSTER", schema = "SCD_API")
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
public class DepartmentCluster implements IEntity<Long> {
    @Id
    @Column(name = "ID_CLUSTER")
    private Long id;
    @ViewField
    @Column(name = "CLUSTER_CODE")
    private String name;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ID_COLLECTION")
    private Collection collection;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ID_BUSINESS")
    private Business business;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ID_CATEGORY_HIERARCHY")
    private CategoryHierarchy category;

    @Override
    @SuppressWarnings("all")
    public boolean equals(Object o) {
        return PersistenceUtils.equals(this, o);
    }

    @Override
    public int hashCode() {
        return isNull(getId()) ? 0 : getId().hashCode();
    }
}
