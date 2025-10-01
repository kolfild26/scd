package ru.sportmaster.scd.service.adjustment.ui;

import static java.util.Optional.ofNullable;
import static ru.sportmaster.scd.consts.ParamNames.DOCUMENT;
import static ru.sportmaster.scd.utils.UiUtil.getId;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.AdjustmentType;
import ru.sportmaster.scd.repository.adjustment.AdjustmentDocRowRepository;
import ru.sportmaster.scd.ui.view.UiViewEditor;
import ru.sportmaster.scd.ui.view.UiViewManager;
import ru.sportmaster.scd.ui.view.type.UiView;
import ru.sportmaster.scd.ui.view.type.UiViewCrudRequest;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdjustmentDocRowEditor implements UiViewEditor {
    private final UiViewManager viewManager;
    private final AdjustmentDocRowRepository adjustmentDocRowRepository;

    @Override
    public ObjectNode change(UiViewCrudRequest request) {
        UiView view = viewManager.getView(request.getView());
        Long documentId = getDocumentId(request);

        AdjustmentType type = AdjustmentType.findByClass(view.getJavaType());
        switch (request.getType()) {
            case CREATE -> createObject(type, request.getValue(), documentId);
            case UPDATE -> updateObject(type, request.getValue(), documentId);
            case DELETE -> deleteObject(type, request.getValue(), documentId);
            default -> throw new NotImplementedException("Operation not implemented!");
        }

        return null;
    }

    private void createObject(AdjustmentType type, ObjectNode value, Long documentId) {
        adjustmentDocRowRepository.create(documentId, type.mapToStruct(value));
    }

    private void updateObject(AdjustmentType type, ObjectNode value, Long documentId) {
        adjustmentDocRowRepository.update(documentId, type.mapToStruct(value));
    }

    private void deleteObject(AdjustmentType type, ObjectNode value, Long documentId) {
        adjustmentDocRowRepository.delete(documentId, type.mapToStruct(value));
    }

    @Nullable
    private static Long getDocumentId(UiViewCrudRequest request) {
        return ofNullable(request.getValue())
            .map(node -> getId(node, DOCUMENT))
            .orElse(null);
    }
}
