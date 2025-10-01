package ru.sportmaster.scd.entity.mementity;

import static ru.sportmaster.scd.consts.ParamNames.FIELD_SEPARATOR;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.NonNull;
import ru.sportmaster.scd.mementity.IMemEntityGetter;
import ru.sportmaster.scd.ui.view.annotation.ViewField;

@MappedSuperclass
//Lombok
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WareME implements IMemEntityGetter {
    @ViewField
    @Column(name = "NAME")
    private String name;
    @ViewField
    @Column(name = "NAME_ENG")
    private String nameEng;
    @Column(name = "ID_MODEL", insertable = false, updatable = false)
    protected Long modelId;
    @ViewField
    @Column(name = "MODEL_CODE")
    protected String modelCode;
    @Column(name = "ID_COLOR")
    protected Long colorId;
    @ViewField
    @Column(name = "COLOR_CODE")
    protected String colorCode;
    @Column(name = "COLOR_NAME")
    protected String colorName;
    @ViewField
    @Column(name = "GRP_OF_WARE")
    protected String group;
    @ViewField
    @Column(name = "CATEGORY")
    protected String category;
    @ViewField
    @Column(name = "SUBCATEGORY")
    protected String subCategory;
    @ViewField
    @Column(name = "GRP")
    protected String wareClass;
    @ViewField
    @Column(name = "SUBGRP")
    protected String wareSubClass;
    @ViewField
    @Column(name = "BRAND")
    protected String brand;
    @ViewField
    @Column(name = "GENDER")
    protected String gender;
    @ViewField
    @Column(name = "AGE")
    protected String age;
    @ViewField
    @Column(name = "COLOR_SIZE")
    protected String size;
    @Column(name = "ID_EXT_SIZE")
    protected Long extSizeId;
    @Column(name = "EXT_SIZE_NAME")
    protected String extSizeName;

    @SuppressWarnings("unchecked")
    @Override
    public Comparable<Object> getFieldValue(String fieldName) {
        return
            (Comparable<Object>) Optional.ofNullable(fieldName)
                .map(this::getValue)
                .orElse(null);
    }

    protected Object getValue(@NonNull String fieldName) {
        return switch (fieldName) {
            case "name" -> getName();
            case "nameEng" -> getNameEng();
            case "modelId" -> getModelId();
            case "modelCode" -> getModelCode();
            case "colorId" -> getColorId();
            case "colorCode" -> getColorCode();
            case "colorName" -> getColorName();
            case "group" -> getGroup();
            case "category" -> getCategory();
            case "subCategory" -> getSubCategory();
            case "wareClass" -> getWareClass();
            case "wareSubClass" -> getWareSubClass();
            case "brand" -> getBrand();
            case "gender" -> getGender();
            case "age" -> getAge();
            case "size" -> getSize();
            case "extSizeId" -> getExtSizeId();
            case "extSizeName" -> getExtSizeName();
            default -> null;
        };
    }

    public String toSearchString() {
        return getName() + FIELD_SEPARATOR
            + getNameEng() + FIELD_SEPARATOR
            + getModelCode() + FIELD_SEPARATOR
            + getColorCode() + FIELD_SEPARATOR
            + getGroup() + FIELD_SEPARATOR
            + getCategory() + FIELD_SEPARATOR
            + getSubCategory() + FIELD_SEPARATOR
            + getWareClass() + FIELD_SEPARATOR
            + getWareSubClass() + FIELD_SEPARATOR
            + getBrand() + FIELD_SEPARATOR
            + getGender() + FIELD_SEPARATOR
            + getAge() + FIELD_SEPARATOR
            + getSize();
    }
}
