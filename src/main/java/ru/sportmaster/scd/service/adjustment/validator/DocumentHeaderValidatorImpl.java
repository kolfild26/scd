package ru.sportmaster.scd.service.adjustment.validator;

import static ru.sportmaster.scd.utils.JsonUtil.getAllFields;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
public class DocumentHeaderValidatorImpl implements DocumentHeaderValidator {
    private final MessageSource messageSource;

    @Override
    public List<AdjustmentValidationErrorDto> validate(UiView view, ObjectNode node, long index) {
        List<AdjustmentValidationErrorDto> errors = new ArrayList<>();
        List<UiViewAttribute> attributes = view.getAttributes();

        List<String> fields = getAllFields(node);
        for (int i = 0; i < attributes.size(); i++) {
            UiViewAttribute attribute = attributes.get(i);
            String label = getLabel(attribute);

            if (fields.size() <= i || !Objects.equals(label, fields.get(i))) {
                errors.add(AdjustmentValidationErrorDto.builder()
                    .key(ValidationError.FIELD_NOT_FOUND)
                    .field(label)
                    .build());
            }
        }
        return errors;
    }

    private String getLabel(UiViewAttribute attribute) {
        return JsonUtil.getLocalizedMessage(messageSource, attribute.getLabel());
    }
}
