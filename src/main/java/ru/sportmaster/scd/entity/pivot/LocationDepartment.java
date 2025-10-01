package ru.sportmaster.scd.entity.pivot;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;
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
import ru.sportmaster.scd.entity.dictionary.Affiliate;
import ru.sportmaster.scd.entity.dictionary.DepartmentCluster;
import ru.sportmaster.scd.entity.dictionary.ShpRefBranch;
import ru.sportmaster.scd.entity.dictionary.Territory;
import ru.sportmaster.scd.entity.dictionary.TradeRegion;
import ru.sportmaster.scd.entity.planlimit.PlProject;
import ru.sportmaster.scd.ui.view.annotation.View;
import ru.sportmaster.scd.ui.view.annotation.ViewField;
import ru.sportmaster.scd.ui.view.annotation.ViewRenderer;
import ru.sportmaster.scd.utils.PersistenceUtils;

@View
@Entity
@Table(name = "ASCD_V_LOCATION_DEPARTMENT", schema = "SCD_API")
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
public class LocationDepartment implements IEntity<Long> {
    @Id
    @Column(name = "ID_DEPARTMENT", precision = 38, nullable = false, updatable = false)
    private Long id;
    @ViewField(order = 0)
    @Column(name = "NAME")
    private String name;
    @ViewField(order = 1)
    @Column(name = "MONIKER_RUS")
    private String moniker;
    @ViewField(order = 2)
    @Column(name = "ID_OBJ")
    private Integer code;
    @View(flat = true)
    @ViewField(order = 3)
    @ManyToOne
    @JoinColumn(name = "ID_BUSINESS_TMA")
    private Business business;
    @View(flat = true)
    @OneToOne
    @JoinColumn(name = "ID_DIVISION_TMA")
    private DivisionTMA division;
    @View(flat = true)
    @ManyToOne
    @JoinColumn(name = "ID_COUNTRY")
    private Country country;
    @View(flat = true)
    @ManyToOne
    @JoinColumn(name = "ID_SALES_REGION")
    private CountryGroup countryGroup;
    @View(flat = true)
    @ManyToOne
    @JoinColumn(name = "ID_AFFILIATE")
    private Affiliate affiliate;
    @View(flat = true)
    @ManyToOne
    @JoinColumn(name = "ID_TRADE_REGION")
    private TradeRegion region;
    @View(flat = true)
    @ManyToOne
    @JoinColumn(name = "ID_SHPREF_BRANCH")
    private ShpRefBranch shpRefBranch;
    @View(flat = true)
    @ManyToOne
    @JoinColumn(name = "ID_TERRITORY")
    private Territory territory;
    @ViewField
    @Column(name = "DT_OPEN_FACT")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate openDate;
    @ViewField
    @Column(name = "DT_CLOSE_FACT")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate closeDate;
    @ViewField
    @Column(name = "SUPPORT_SHOP_MONIKER")
    private String supportShop;
    @ViewField
    @Column(name = "ID_SHOP")
    private Long shopId;
    @View(flat = true)
    @ManyToOne
    @ViewField(renderer = @ViewRenderer(srcField = "name"))
    @JoinColumn(name = "ID_PROJECT")
    private PlProject project;
    @JsonIgnore
    @Column(name = "PATH")
    private String treePath;
    @JsonIgnore
    @OneToMany
    @JoinColumn(name = "ID_DEPARTMENT")
    private List<DepartmentCluster> clusters;
    @Column(name = "IS_DELETED")
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
