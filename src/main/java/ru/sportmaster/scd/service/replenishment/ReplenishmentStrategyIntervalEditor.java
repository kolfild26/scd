package ru.sportmaster.scd.service.replenishment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.sportmaster.scd.entity.replenishment.ReplenishmentStrategyInterval;
import ru.sportmaster.scd.entity.replenishment.ReplenishmentStrategyNode_;
import ru.sportmaster.scd.ui.editor.JpaViewEditor;
import ru.sportmaster.scd.ui.view.UiViewEditor;
import ru.sportmaster.scd.ui.view.type.UiViewCrudRequest;

/**
 * Редактор для изменения сущности ReplenishmentStrategyInterval.
 * Обновление дат ReplenishmentStrategyNode для текущего интервала.
 * **/
@Component
@RequiredArgsConstructor
public class ReplenishmentStrategyIntervalEditor implements UiViewEditor {
    private static final String UPDATE_REQUEST = "UPDATE SCD.SCD_REPL_STRATEGY_NODES_LINK SET"
        + " DATE_STRAT_BEGIN = :beginDate,"
        + " DATE_STRAT_END = :endDate"
        + " WHERE "
        + "ID_REPL_INTERVAL = :interval";

    private final ObjectMapper objectMapper;
    private final JpaViewEditor viewEditor;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public ObjectNode change(UiViewCrudRequest request) {
        var changed = viewEditor.change(request);
        updateReplStrategyNodeDates(request);
        return changed;
    }

    private void updateReplStrategyNodeDates(UiViewCrudRequest request) {
        var interval = objectMapper.convertValue(request.getValue(), ReplenishmentStrategyInterval.class);
        Query query = prepareRequest(interval);
        query.executeUpdate();
    }

    private Query prepareRequest(ReplenishmentStrategyInterval interval) {
        return entityManager.createNativeQuery(ReplenishmentStrategyIntervalEditor.UPDATE_REQUEST)
            .setParameter(ReplenishmentStrategyNode_.BEGIN_DATE, interval.getBeginDate())
            .setParameter(ReplenishmentStrategyNode_.END_DATE, interval.getEndDate())
            .setParameter(ReplenishmentStrategyNode_.INTERVAL, interval.getId());
    }
}
