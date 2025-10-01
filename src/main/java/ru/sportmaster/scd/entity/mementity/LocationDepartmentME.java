package ru.sportmaster.scd.entity.mementity;

import static ru.sportmaster.scd.consts.ParamNames.FIELD_SEPARATOR;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Immutable;
import ru.sportmaster.scd.entity.IEntity;
import ru.sportmaster.scd.mementity.IMemEntityGetter;
import ru.sportmaster.scd.mementity.LocationDepartmentMemEntityResolver;
import ru.sportmaster.scd.ui.view.annotation.CustomView;
import ru.sportmaster.scd.ui.view.annotation.ViewField;
import ru.sportmaster.scd.utils.PersistenceUtils;

@CustomView(name = "LocationDepartmentME", resolver = LocationDepartmentMemEntityResolver.class)
@Entity
@Table(name = "ASCD_V_LOCATION_DEPARTMENT_ME", schema = "SCD_API")
//Lombok
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
//Hibernate
@Immutable
public class LocationDepartmentME implements IEntity<Long>, IMemEntityGetter {
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
    @ViewField(order = 3)
    @Column(name = "BUSINESS_TMA_RUS")
    private String business;
    @ViewField(order = 4)
    @Column(name = "DIVISION_TMA")
    private String division;
    @ViewField(order = 7)
    @Column(name = "COUNTRY")
    private String country;
    @ViewField(order = 7)
    @Column(name = "COUNTRY_GROUP")
    private String countryGroup;
    @ViewField(order = 9)
    @Column(name = "AFFILIATE")
    private String affiliate;
    @ViewField(order = 10)
    @Column(name = "TRADE_REGION")
    private String region;
    @ViewField(order = 11)
    @Column(name = "SHPREF_BRANCH")
    private String shpRefBranch;
    @ViewField(order = 12)
    @Column(name = "TERRITORY")
    private String territory;
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
    @ViewField
    @Column(name = "PROJECT")
    private String project;
    @JsonIgnore
    @Column(name = "PARTITION")
    private String partition;
    @JsonIgnore
    @Column(name = "DIVISION_SCD")
    private String divisionSCD;
    @JsonIgnore
    @Column(name = "PATH")
    private String treePath;
    @JsonIgnore
    @Column(name = "CLUSTERS")
    private String clusters;

    public static LocationDepartmentME resultSetMapping(ResultSet resultSet) throws SQLException {
        return
            LocationDepartmentME.builder()
                .id(resultSet.getLong("ID_DEPARTMENT"))
                .name(resultSet.getString("NAME"))
                .moniker(resultSet.getString("MONIKER_RUS"))
                .code(resultSet.getInt("ID_OBJ"))
                .business(resultSet.getString("BUSINESS_TMA_RUS"))
                .division(resultSet.getString("DIVISION_TMA"))
                .country(resultSet.getString("COUNTRY"))
                .countryGroup(resultSet.getString("COUNTRY_GROUP"))
                .affiliate(resultSet.getString("AFFILIATE"))
                .region(resultSet.getString("TRADE_REGION"))
                .shpRefBranch(resultSet.getString("SHPREF_BRANCH"))
                .territory(resultSet.getString("TERRITORY"))
                .openDate(
                    Optional.ofNullable(resultSet.getDate("DT_OPEN_FACT"))
                        .map(Date::toLocalDate)
                        .orElse(null)
                )
                .closeDate(
                    Optional.ofNullable(resultSet.getDate("DT_CLOSE_FACT"))
                        .map(Date::toLocalDate)
                        .orElse(null)
                )
                .supportShop(resultSet.getString("SUPPORT_SHOP_MONIKER"))
                .shopId(resultSet.getLong("ID_SHOP"))
                .project(resultSet.getString("PROJECT"))
                .partition(resultSet.getString("PARTITION"))
                .divisionSCD(resultSet.getString("DIVISION_SCD"))
                .treePath(resultSet.getString("PATH"))
                .clusters(resultSet.getString("CLUSTERS"))
                .build();
    }

    public String toSearchString() {
        return getName() + FIELD_SEPARATOR
            + getMoniker() + FIELD_SEPARATOR
            + getCode() + FIELD_SEPARATOR
            + getBusiness() + FIELD_SEPARATOR
            + getDivision() + FIELD_SEPARATOR
            + getCountry() + FIELD_SEPARATOR
            + getCountryGroup() + FIELD_SEPARATOR
            + getAffiliate() + FIELD_SEPARATOR
            + getRegion() + FIELD_SEPARATOR
            + getShpRefBranch() + FIELD_SEPARATOR
            + getTerritory() + FIELD_SEPARATOR
            + getOpenDate() + FIELD_SEPARATOR
            + getCloseDate() + FIELD_SEPARATOR
            + getSupportShop() + FIELD_SEPARATOR
            + getShopId() + FIELD_SEPARATOR
            + getProject();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Comparable<Object> getFieldValue(String fieldName) {
        return
            (Comparable<Object>) Optional.ofNullable(fieldName)
                .map(this::getValue)
                .orElse(null);
    }

    private Object getValue(String fieldName) {
        return switch (fieldName) {
            case "id" -> getId();
            case "name" -> getName();
            case "moniker" -> getMoniker();
            case "code" -> getCode();
            case "business" -> getBusiness();
            case "division" -> getDivision();
            case "country" -> getCountry();
            case "countryGroup" -> getCountryGroup();
            case "affiliate" -> getAffiliate();
            case "region" -> getRegion();
            case "shpRefBranch" -> getShpRefBranch();
            case "territory" -> getTerritory();
            case "openDate" -> getOpenDate();
            case "closeDate" -> getCloseDate();
            case "supportShop" -> getSupportShop();
            case "shopId" -> getShopId();
            case "project" -> getProject();
            case "partition" -> getPartition();
            case "divisionSCD" -> getDivisionSCD();
            case "treePath" -> getTreePath();
            case "clusters" -> getClusters();
            default -> null;
        };
    }

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
