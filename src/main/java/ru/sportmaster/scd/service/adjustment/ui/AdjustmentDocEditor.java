package ru.sportmaster.scd.service.adjustment.ui;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.AdjustmentDocType_;
import ru.sportmaster.scd.entity.adjustment.AdjustmentDoc_;
import ru.sportmaster.scd.entity.adjustment.Business_;
import ru.sportmaster.scd.repository.adjustment.AdjustmentDocRepository;
import ru.sportmaster.scd.repository.adjustment.AdjustmentUserRepository;
import ru.sportmaster.scd.service.AuthService;
import ru.sportmaster.scd.ui.view.UiViewEditor;
import ru.sportmaster.scd.ui.view.type.UiViewCrudRequest;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdjustmentDocEditor implements UiViewEditor {
    private final AuthService authService;
    private final AdjustmentDocRepository adjustmentDocRepository;
    private final AdjustmentUserRepository adjustmentUserRepository;

    @Override
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
        Long typeId = request.getValue().get(AdjustmentDoc_.TYPE).get(AdjustmentDocType_.ID).asLong();
        String description = request.getValue().get(AdjustmentDoc_.COMMENT).asText("");
        Long businessId = request.getValue().get(AdjustmentDoc_.BUSINESS).get(Business_.ID).asLong();
        adjustmentDocRepository.create(typeId, description, businessId, getDcRefUserId());
    }

    private void updateObject(UiViewCrudRequest request) {
        String description = request.getValue().get(AdjustmentDoc_.COMMENT).asText("");
        adjustmentDocRepository.update(request.getId(), description, getDcRefUserId());
    }

    private void deleteObject(UiViewCrudRequest request) {
        adjustmentDocRepository.delete(request.getId(), getDcRefUserId());
    }

    private Long getDcRefUserId() {
        Long userId = authService.getCurrentUser().getId();
        return adjustmentUserRepository.getDcUserId(userId);
    }
}
