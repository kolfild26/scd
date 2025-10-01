package ru.sportmaster.scd.service.adjustment.validator;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import ru.sportmaster.scd.dto.adjustment.AdjustmentValidationErrorDto;
import ru.sportmaster.scd.entity.adjustment.AdjustmentType;
import ru.sportmaster.scd.ui.view.type.UiView;

public interface DocumentRowValidator {
    default boolean isSupported(AdjustmentType type) {
        return true;
    }

    List<AdjustmentValidationErrorDto> validate(UiView view, ObjectNode node, long index);
}
