package ru.sportmaster.scd.entity.planlimit;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.sportmaster.scd.persistence.ListLongConverter;
import ru.sportmaster.scd.ui.view.annotation.View;

@View
@Entity
@Table(name = "ASCD_V_RPAS_CITY_GROUPED", schema = "SCD_API")
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
public class PlGroupedCity {
    @Id
    @Column(name = "ID_GROUP")
    private Long id;
    @Column(name = "MONIKER")
    private String name;
    @ManyToOne
    @JoinColumn(name = "ID_BUSINESS")
    private PlBusiness business;
    @Column(name = "ID_CITIES")
    @Convert(converter = ListLongConverter.class)
    private List<Long> cities;
}
