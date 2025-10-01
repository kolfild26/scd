package ru.sportmaster.scd.service.adjustment.validator;

import static java.util.Objects.nonNull;
import static ru.sportmaster.scd.entity.adjustment.ScaleSize_.NAME;
import static ru.sportmaster.scd.utils.JsonUtil.getAllFields;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.dto.adjustment.AdjustmentValidationErrorDto;
import ru.sportmaster.scd.dto.adjustment.ValidationError;
import ru.sportmaster.scd.entity.adjustment.AdjustmentType;
import ru.sportmaster.scd.entity.adjustment.ScaleSize;
import ru.sportmaster.scd.repository.adjustment.AdjustmentDocRowValuesRepository;
import ru.sportmaster.scd.ui.view.type.UiView;
import ru.sportmaster.scd.ui.view.type.UiViewAttribute;
import ru.sportmaster.scd.utils.JsonUtil;

@SuppressWarnings("CPD-START")
@Component
@RequiredArgsConstructor
public class DocumentScaleSizeRowValidator implements DocumentRowValidator {
    private static final List<AdjustmentType> SUPPORTED_TYPES = List.of(
        AdjustmentType.MIN_UPA_MSC,
        AdjustmentType.MIN_UPA_DSC,
        AdjustmentType.PLAN_LIMIT_MSC,
        AdjustmentType.PLAN_LIMIT_DSC
    );

    private final MessageSource messageSource;
    private final AdjustmentDocRowValuesRepository adjustmentDocRowValuesRepository;

    @Override
    public boolean isSupported(AdjustmentType type) {
        return SUPPORTED_TYPES.contains(type);
    }

    @Override
    public List<AdjustmentValidationErrorDto> validate(UiView view, ObjectNode node, long index) {
        var errors = new ArrayList<AdjustmentValidationErrorDto>();

        var fields = getAllFields(node);
        var attributes = view.getAttributes();

        for (int i = 0; i < attributes.size(); i++) {
            var attribute = attributes.get(i);

            try {
                if (ScaleSize.class.equals(attribute.getSourceType())) {
                    var valueNode = fields.size() > i ? node.get(fields.get(i)) : null;
                    if (nonNull(valueNode) && !valueNode.isNull()
                        && !adjustmentDocRowValuesRepository.hasColumnValue(
                            ScaleSize.class, NAME, valueNode.asText())) {
                        errors.add(buildError(index, i, attribute));
                    }
                }
            } catch (Exception ex) {
                errors.add(buildError(index, i, attribute));
            }
        }

        return errors;
    }

    private AdjustmentValidationErrorDto buildError(long rowIndex, long colIndex, UiViewAttribute attribute) {
        return AdjustmentValidationErrorDto.builder()
            .rowIndex(rowIndex - 1)
            .colIndex(colIndex)
            .key(ValidationError.CELL_VALUE_NOT_FOUND)
            .field(getLabel(attribute))
            .build();
    }

    private String getLabel(UiViewAttribute attribute) {
        return JsonUtil.getLocalizedMessage(messageSource, attribute.getLabel());
    }
}
