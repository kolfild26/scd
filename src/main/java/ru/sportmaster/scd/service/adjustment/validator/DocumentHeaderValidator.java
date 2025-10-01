package ru.sportmaster.scd.service.adjustment.validator;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import ru.sportmaster.scd.dto.adjustment.AdjustmentValidationErrorDto;
import ru.sportmaster.scd.ui.view.type.UiView;

public interface DocumentHeaderValidator {
    List<AdjustmentValidationErrorDto> validate(UiView view, ObjectNode node, long index);
}
