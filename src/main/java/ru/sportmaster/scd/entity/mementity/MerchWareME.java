package ru.sportmaster.scd.entity.mementity;

import static java.util.Optional.of;
import static ru.sportmaster.scd.consts.ParamNames.ID;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import org.springframework.lang.NonNull;
import ru.sportmaster.scd.entity.IEntity;
import ru.sportmaster.scd.mementity.MerchWareMemEntityResolver;
import ru.sportmaster.scd.ui.view.annotation.CustomView;
import ru.sportmaster.scd.utils.PersistenceUtils;

@CustomView(name = "MerchWareME", resolver = MerchWareMemEntityResolver.class)
@Entity
@Table(name = "ASCD_V_MERCH_WARE_ME", schema = "SCD_API")
//Lombok
@SuperBuilder(builderMethodName = "merchWareBuilder")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
//Hibernate
@Immutable
@Cacheable
@Cache(region = "referenceCache", usage = CacheConcurrencyStrategy.READ_ONLY)
public class MerchWareME extends WareME implements IEntity<Long> {
    @Id
    @Column(name = "ID_MERCH_COLOR_SIZE", precision = 38, nullable = false, updatable = false)
    private Long id;

    public static MerchWareME resultSetMapping(ResultSet resultSet) throws SQLException {
        return
            MerchWareME.merchWareBuilder()
                .id(resultSet.getLong("ID_MERCH_COLOR_SIZE"))
                .name(resultSet.getString("NAME"))
                .nameEng(resultSet.getString("NAME_ENG"))
                .modelId(resultSet.getLong("ID_MODEL"))
                .modelCode(resultSet.getString("MODEL_CODE"))
                .colorId(resultSet.getLong("ID_COLOR"))
                .colorCode(resultSet.getString("COLOR_CODE"))
                .group(resultSet.getString("GRP_OF_WARE"))
                .category(resultSet.getString("CATEGORY"))
                .subCategory(resultSet.getString("SUBCATEGORY"))
                .wareClass(resultSet.getString("GRP"))
                .wareSubClass(resultSet.getString("SUBGRP"))
                .brand(resultSet.getString("BRAND"))
                .gender(resultSet.getString("GENDER"))
                .age(resultSet.getString("AGE"))
                .size(resultSet.getString("COLOR_SIZE"))
                .extSizeId(of(resultSet.getLong("ID_EXT_SIZE")).filter(i -> i != 0).orElse(null))
                .extSizeName(resultSet.getString("EXT_SIZE_NAME"))
                .build();
    }

    @Override
    protected Object getValue(@NonNull String fieldName) {
        if (ID.equals(fieldName)) {
            return getId();
        }
        return super.getValue(fieldName);

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
