package ru.sportmaster.scd.entity.returnlogistic;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;
import ru.sportmaster.scd.entity.pivot.LocationDepartment;
import ru.sportmaster.scd.ui.view.annotation.View;
import ru.sportmaster.scd.ui.view.annotation.ViewField;

@View
@Entity
@Table(name = "SCD_RL_SHOP_EXCLUDING", schema = "SCD")
//Lombok
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
//Hibernate
@Cacheable
@Cache(region = "referenceCache", usage = CacheConcurrencyStrategy.READ_WRITE)
public class ShopExcluding {
    @Id
    @Column(name = "ID_SHOP_EXCLUDING")
    private Long id;

    @ManyToOne
    @MapsId(value = "locationDepartmentId")
    @JoinColumn(name = "ID_DEPARTMENT")
    @ViewField(order = 0)
    private LocationDepartment locationDepartment;

    @ManyToOne
    @MapsId(value = "returnTypeId")
    @JoinColumn(name = "ID_RETURN_TYPE")
    @ViewField(order = 1)
    private ReturnType returnType;
}
