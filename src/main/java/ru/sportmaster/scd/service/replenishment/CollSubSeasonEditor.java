package ru.sportmaster.scd.service.replenishment;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.sportmaster.scd.entity.replenishment.CollSubSeason;
import ru.sportmaster.scd.entity.replenishment.CollSubSeasonKey;
import ru.sportmaster.scd.entity.replenishment.CollSubSeason_;
import ru.sportmaster.scd.entity.replenishment.SubSeason;
import ru.sportmaster.scd.ui.editor.JpaViewEditor;
import ru.sportmaster.scd.ui.view.UiViewEditor;
import ru.sportmaster.scd.ui.view.type.ChangeType;
import ru.sportmaster.scd.ui.view.type.UiViewCrudRequest;

/**
 * Редактор для изменения сущности CollSubSeason, обновление subSeasonId при редактировании строки.
 * Hibernate не позволяет обновить subSeason, т.к. он используется в EmbeddedId.
 * **/
@Component
@RequiredArgsConstructor
public class CollSubSeasonEditor implements UiViewEditor {
    private static final String UPDATE_REQUEST = "UPDATE SCD.SCD_COLL_SUBSEASON_LINK SET"
        + " ID_SUBSEASON = :subSeason"
        + " WHERE "
        + "ID_COLLECTION = ?1 AND "
        + "ID_SUBSEASON = ?2 AND "
        + "ID_DIVISION_TMA = ?3 AND "
        + "ID_BUSINESS_TMA = ?4";

    private final ObjectMapper objectMapper;
    private final JpaViewEditor viewEditor;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public ObjectNode change(UiViewCrudRequest request) {
        if (ChangeType.UPDATE == request.getType()) {
            return updateObject(request);
        }

        return viewEditor.change(request);
    }

    private ObjectNode updateObject(UiViewCrudRequest request) {
        ObjectNode updatable = viewEditor.change(request);
        CollSubSeason settings = objectMapper.convertValue(request.getValue(), CollSubSeason.class);
        Query query = putCollSubSeasonWhereParams(prepareRequest(settings), settings);
        query.executeUpdate();

        return updatable;
    }

    private Query putCollSubSeasonWhereParams(Query query, CollSubSeason settings) {
        return query
            .setParameter(1, of(settings.getId()).map(CollSubSeasonKey::getCollectionId).orElse(null))
            .setParameter(2, of(settings.getId()).map(CollSubSeasonKey::getSubSeasonId).orElse(null))
            .setParameter(3, of(settings.getId()).map(CollSubSeasonKey::getDivisionId).orElse(null))
            .setParameter(4, of(settings.getId()).map(CollSubSeasonKey::getBusinessId).orElse(null));
    }

    private Query prepareRequest(CollSubSeason settings) {
        return entityManager.createNativeQuery(CollSubSeasonEditor.UPDATE_REQUEST)
            .setParameter(CollSubSeason_.SUB_SEASON,
                ofNullable(settings.getSubSeason()).map(SubSeason::getId).orElse(null));
    }
}
