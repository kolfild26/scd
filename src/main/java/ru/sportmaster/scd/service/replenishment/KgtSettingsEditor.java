package ru.sportmaster.scd.service.replenishment;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.sportmaster.scd.entity.adjustment.Business;
import ru.sportmaster.scd.entity.dictionary.Category;
import ru.sportmaster.scd.entity.dictionary.SubCategory;
import ru.sportmaster.scd.entity.dictionary.WareClass;
import ru.sportmaster.scd.entity.dictionary.WareGroup;
import ru.sportmaster.scd.entity.replenishment.KgtSettings;
import ru.sportmaster.scd.entity.replenishment.KgtSettingsKey;
import ru.sportmaster.scd.entity.replenishment.KgtSettings_;
import ru.sportmaster.scd.ui.view.UiViewEditor;
import ru.sportmaster.scd.ui.view.type.UiViewCrudRequest;

/**
 * Редактор для изменения сущности KgtSettings с не полным составным ключем.
 * Рассмотреть добавление PK в таблицу настроек КГТ.
 * **/
@Component
@RequiredArgsConstructor
public class KgtSettingsEditor implements UiViewEditor {
    private static final String INSERT_REQUEST = format("INSERT INTO SCD.SCD_IS_KGT_SETTINGS"
            + "(ID_GRP_OF_WARE, ID_CATEGORY, ID_SUBCATEGORY, ID_GRP, ID_BUSINESS_TMA)"
            + " VALUES (:%s, :%s, :%s, :%s, :%s)",
        KgtSettings_.WARE_GROUP,
        KgtSettings_.CATEGORY,
        KgtSettings_.SUB_CATEGORY,
        KgtSettings_.WARE_CLASS,
        KgtSettings_.BUSINESS);
    private static final String UPDATE_REQUEST = "UPDATE SCD.SCD_IS_KGT_SETTINGS SET"
        + " ID_GRP_OF_WARE = :wareGroup, ID_CATEGORY = :category,"
        + " ID_SUBCATEGORY = :subCategory, ID_GRP = :wareClass, ID_BUSINESS_TMA = :business"
        + " WHERE "
        + "((ID_GRP_OF_WARE IS NULL AND ?1 IS NULL) OR ID_GRP_OF_WARE = ?1) AND "
        + "((ID_CATEGORY IS NULL AND ?2 IS NULL) OR ID_CATEGORY = ?2) AND "
        + "((ID_SUBCATEGORY IS NULL AND ?3 IS NULL) OR ID_SUBCATEGORY = ?3) AND "
        + "((ID_GRP IS NULL AND ?4 IS NULL) OR ID_GRP = ?4) AND "
        + "ID_BUSINESS_TMA = ?5";

    private static final String DELETE_REQUEST = "DELETE FROM SCD.SCD_IS_KGT_SETTINGS "
            + "WHERE "
            + "((ID_GRP_OF_WARE IS NULL AND ?1 IS NULL) OR ID_GRP_OF_WARE = ?1) AND "
            + "((ID_CATEGORY IS NULL AND ?2 IS NULL) OR ID_CATEGORY = ?2) AND "
            + "((ID_SUBCATEGORY IS NULL AND ?3 IS NULL) OR ID_SUBCATEGORY = ?3) AND "
            + "((ID_GRP IS NULL AND ?4 IS NULL) OR ID_GRP = ?4) AND "
            + "ID_BUSINESS_TMA = ?5";

    private final ObjectMapper objectMapper;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public ObjectNode change(UiViewCrudRequest request) {
        switch (request.getType()) {
            case CREATE -> createObject(request);
            case UPDATE -> updateObject(request);
            case DELETE -> deleteObject(request);
            default -> throw new NotImplementedException("Operation not implemented!");
        }

        return null;
    }

    private void createObject(UiViewCrudRequest request) {
        KgtSettings settings = objectMapper.convertValue(request.getValue(), KgtSettings.class);
        Query query = prepareRequest(INSERT_REQUEST, settings);
        query.executeUpdate();
    }

    private void updateObject(UiViewCrudRequest request) {
        KgtSettings settings = objectMapper.convertValue(request.getValue(), KgtSettings.class);

        Query query = putKgtKeyParameters(prepareRequest(UPDATE_REQUEST, settings), settings);
        query.executeUpdate();
    }

    private void deleteObject(UiViewCrudRequest request) {
        KgtSettings settings = objectMapper.convertValue(request.getValue(), KgtSettings.class);

        Query query = putKgtKeyParameters(entityManager.createNativeQuery(DELETE_REQUEST), settings);
        query.executeUpdate();
    }

    private Query putKgtKeyParameters(Query query, KgtSettings settings) {
        return query
            .setParameter(1, ofNullable(settings.getId()).map(KgtSettingsKey::getWareGroupId).orElse(null))
            .setParameter(2, ofNullable(settings.getId()).map(KgtSettingsKey::getCategoryId).orElse(null))
            .setParameter(3, ofNullable(settings.getId()).map(KgtSettingsKey::getSubCategoryId).orElse(null))
            .setParameter(4, ofNullable(settings.getId()).map(KgtSettingsKey::getWareClassId).orElse(null))
            .setParameter(5, ofNullable(settings.getId()).map(KgtSettingsKey::getBusinessId).orElse(null));
    }

    private Query prepareRequest(String request, KgtSettings settings) {
        return entityManager.createNativeQuery(request)
            .setParameter(KgtSettings_.WARE_GROUP,
                ofNullable(settings.getWareGroup()).map(WareGroup::getId).orElse(null))
            .setParameter(KgtSettings_.CATEGORY,
                ofNullable(settings.getCategory()).map(Category::getId).orElse(null))
            .setParameter(KgtSettings_.SUB_CATEGORY,
                ofNullable(settings.getSubCategory()).map(SubCategory::getId).orElse(null))
            .setParameter(KgtSettings_.WARE_CLASS,
                ofNullable(settings.getWareClass()).map(WareClass::getId).orElse(null))
            .setParameter(KgtSettings_.BUSINESS,
                ofNullable(settings.getBusiness()).map(Business::getId).orElse(null));
    }
}
