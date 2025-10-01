package ru.sportmaster.scd.service.adjustment.validator;

import static java.util.Objects.isNull;
import static ru.sportmaster.scd.utils.ConvertUtil.isDateValue;
import static ru.sportmaster.scd.utils.JsonUtil.getAllFields;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.dto.adjustment.AdjustmentValidationErrorDto;
import ru.sportmaster.scd.dto.adjustment.ValidationError;
import ru.sportmaster.scd.ui.view.type.UiView;
import ru.sportmaster.scd.ui.view.type.UiViewAttribute;
import ru.sportmaster.scd.utils.JsonUtil;

@Component
@RequiredArgsConstructor
public class DocumentLocalDateRowValidator implements DocumentRowValidator {
    private static final Predicate<Class<?>> LOCAL_DATE_TYPE = clazz -> clazz.equals(LocalDate.class);
    private final MessageSource messageSource;

    @Override
    public List<AdjustmentValidationErrorDto> validate(UiView view, ObjectNode node, long index) {
        List<AdjustmentValidationErrorDto> errors = new ArrayList<>();
        List<String> fields = getAllFields(node);
        List<UiViewAttribute> attributes = view.getAttributes();

        for (int i = 0; i < attributes.size(); i++) {
            UiViewAttribute attribute = attributes.get(i);

            try {
                if (LOCAL_DATE_TYPE.test(attribute.getJavaType())) {
                    var valueNode = fields.size() > i ? node.get(fields.get(i)) : null;
                    var isNull = isNull(valueNode) || valueNode.isNull();

                    var isAllowNull = attribute.isRequired() && isNull;
                    var isDateNotValid = !isNull && !isDateValue(valueNode.asText());

                    if (isAllowNull || isDateNotValid) {
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
            .key(ValidationError.CELL_TYPE_NOT_VALID)
            .field(getLabel(attribute))
            .build();
    }

    private String getLabel(UiViewAttribute attribute) {
        return JsonUtil.getLocalizedMessage(messageSource, attribute.getLabel());
    }
}
