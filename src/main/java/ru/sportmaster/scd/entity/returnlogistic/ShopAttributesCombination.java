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
import ru.sportmaster.scd.entity.dictionary.Partition;
import ru.sportmaster.scd.entity.pivot.CountryGroup;
import ru.sportmaster.scd.ui.view.annotation.View;
import ru.sportmaster.scd.ui.view.annotation.ViewField;

@View
@Entity
@Table(name = "SCD_RL_SHOP_ATTRIBUTES_COMBINATION", schema = "SCD")
//Lombok
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
//Hibernate
@Cacheable
@Cache(region = "referenceCache", usage = CacheConcurrencyStrategy.READ_WRITE)
public class ShopAttributesCombination {
    @Id
    @Column(name = "ID_SHOP_ATTRIBUTES_COMBINATION")
    private Long id;

    @ManyToOne
    @MapsId(value = "countryGroupId")
    @JoinColumn(name = "ID_COUNTRY_GROUP")
    @ViewField(editable = false, order = 0)
    private CountryGroup countryGroup;

    @ManyToOne
    @MapsId(value = "partitionId")
    @JoinColumn(name = "ID_PARTITION_DIV_TMA")
    @ViewField(editable = false, order = 1)
    private Partition partition;

    @Column(name = "IS_AVAILABLE", precision = 1)
    @ViewField(order = 2)
    private Boolean value;
}
