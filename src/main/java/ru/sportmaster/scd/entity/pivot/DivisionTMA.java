package ru.sportmaster.scd.entity.pivot;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
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
import ru.sportmaster.scd.entity.dictionary.Partition;
import ru.sportmaster.scd.ui.view.annotation.View;
import ru.sportmaster.scd.ui.view.annotation.ViewField;
import ru.sportmaster.scd.utils.PersistenceUtils;

@View
@Entity
@Table(name = "SCD_MDM_DIVISION_TMA", schema = "SCD")
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
public class DivisionTMA implements IEntity<Long> {
    @Id
    @Column(name = "ID_DIVISION_TMA", precision = 38, nullable = false, updatable = false)
    private Long id;
    @ViewField(order = 4)
    @Column(name = "DIVISION_TMA")
    private String name;
    @JsonIgnore
    @View(flat = true)
    @ViewField
    @ManyToOne
    @JoinTable(
        schema = "SCD",
        name = "SCD_PARTITION_DIV_TMA_LINKS",
        joinColumns = @JoinColumn(name = "ID_DIVISION_TMA"),
        inverseJoinColumns = @JoinColumn(name = "ID_PARTITION_DIV_TMA")
    )
    private Partition partition;

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
