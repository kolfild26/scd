package ru.sportmaster.scd.service.adjustment.validator;

import static java.time.LocalDate.parse;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static ru.sportmaster.scd.consts.ParamNames.ID;
import static ru.sportmaster.scd.utils.JsonUtil.getAllFields;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.dto.adjustment.AdjustmentValidationErrorDto;
import ru.sportmaster.scd.dto.adjustment.ValidationError;
import ru.sportmaster.scd.entity.dictionary.Day;
import ru.sportmaster.scd.repository.adjustment.AdjustmentDocRowValuesRepository;
import ru.sportmaster.scd.ui.view.type.UiView;
import ru.sportmaster.scd.ui.view.type.UiViewAttribute;
import ru.sportmaster.scd.utils.JsonUtil;
import ru.sportmaster.scd.utils.UiUtil;

@SuppressWarnings("CPD-START")
@Component
@RequiredArgsConstructor
public class DocumentDayRowValidator implements DocumentRowValidator {
    private final MessageSource messageSource;
    private final AdjustmentDocRowValuesRepository adjustmentDocRowValuesRepository;

    @Override
    public List<AdjustmentValidationErrorDto> validate(UiView view, ObjectNode node, long index) {
        var errors = new ArrayList<AdjustmentValidationErrorDto>();
        var fields = getAllFields(node);
        var attributes = view.getAttributes();

        for (int i = 0; i < attributes.size(); i++) {
            var attribute = attributes.get(i);

            try {
                if (Day.class.equals(attribute.getJavaType())) {
                    var valueNode = fields.size() > i ? node.get(fields.get(i)) : null;

                    if (attribute.isRequired() && (isNull(valueNode) || valueNode.isNull())) {
                        errors.add(buildError(index, i, attribute));
                    } else if (nonNull(valueNode)) {
                        var value = parse(valueNode.asText(), UiUtil.DATE_FORMAT)
                            .format(UiUtil.DB_CALENDAR_ID_FORMAT);
                        if (!adjustmentDocRowValuesRepository.hasColumnValue(Day.class, ID, value)) {
                            errors.add(buildError(index, i, attribute));
                        }
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
