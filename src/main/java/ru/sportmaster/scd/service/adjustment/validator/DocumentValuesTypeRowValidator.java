package ru.sportmaster.scd.service.adjustment.validator;

import static org.springframework.util.ClassUtils.isPrimitiveOrWrapper;
import static ru.sportmaster.scd.utils.JsonUtil.convertType;
import static ru.sportmaster.scd.utils.JsonUtil.getAllFields;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.sportmaster.scd.dto.adjustment.AdjustmentValidationErrorDto;
import ru.sportmaster.scd.dto.adjustment.ValidationError;
import ru.sportmaster.scd.repository.adjustment.AdjustmentDocRowValuesRepository;
import ru.sportmaster.scd.ui.view.UiViewManager;
import ru.sportmaster.scd.ui.view.type.UIViewAttributeEditor;
import ru.sportmaster.scd.ui.view.type.UiView;
import ru.sportmaster.scd.ui.view.type.UiViewAttribute;
import ru.sportmaster.scd.utils.ConvertUtil;
import ru.sportmaster.scd.utils.JsonUtil;

@Component
@RequiredArgsConstructor
public class DocumentValuesTypeRowValidator implements DocumentRowValidator {
    private static final Predicate<Class<?>> PRIMITIVE_FIELDS = clazz ->
        clazz.equals(LocalDateTime.class)
            || clazz.equals(LocalDate.class)
            || clazz.equals(String.class)
            || isPrimitiveOrWrapper(clazz);
    private final UiViewManager viewManager;
    private final MessageSource messageSource;
    private final AdjustmentDocRowValuesRepository adjustmentDocRowValuesRepository;

    @Override
    public List<AdjustmentValidationErrorDto> validate(UiView view, ObjectNode node, long index) {
        List<AdjustmentValidationErrorDto> errors = new ArrayList<>();
        List<String> fields = getAllFields(node);
        List<UiViewAttribute> attributes = view.getAttributes();

        for (int i = 0; i < attributes.size(); i++) {
            UiViewAttribute attribute = attributes.get(i);
            UIViewAttributeEditor editor = attribute.getEditor();
            JsonNode valueNode = fields.size() > i ? node.get(fields.get(i)) : null;
            boolean isNotBlank = valueNode != null && !valueNode.asText().isBlank();

            try {
                if (attribute.isEditable() && attribute.isRequired()) {
                    if (editor != null && viewManager.isView(editor.getType())) {
                        UiView fieldView = viewManager.getView(editor.getType());
                        Object value = convertType(attribute.getJavaType(), valueNode);
                        String path = attribute.getName().replace(editor.getName() + ".", "");
                        if (isNotBlank
                            && !adjustmentDocRowValuesRepository.hasColumnValue(fieldView.getJavaType(), path, value)) {
                            errors.add(AdjustmentValidationErrorDto.builder()
                                .rowIndex(index - 1)
                                .colIndex((long) i)
                                .key(ValidationError.CELL_VALUE_NOT_FOUND)
                                .field(getLabel(attribute))
                                .build());
                        }
                    } else if (editor == null || PRIMITIVE_FIELDS.test(attribute.getJavaType())) {
                        if (isNotBlank && !validPrimitiveType(attribute.getJavaType(), valueNode)) {
                            errors.add(AdjustmentValidationErrorDto.builder()
                                .rowIndex(index - 1)
                                .colIndex((long) i)
                                .key(ValidationError.CELL_TYPE_NOT_VALID)
                                .field(getLabel(attribute))
                                .build());
                        }
                    }
                }
            } catch (Exception ex) {
                errors.add(AdjustmentValidationErrorDto.builder()
                    .rowIndex(index - 1)
                    .colIndex((long) i)
                    .key(ValidationError.CELL_TYPE_NOT_VALID)
                    .field(getLabel(attribute))
                    .build());
            }
        }

        return errors;
    }

    private boolean validPrimitiveType(Class<?> clazz, JsonNode value) {
        if (clazz == Boolean.class || clazz == boolean.class) {
            return BooleanUtils.toBooleanObject(value.asText()) != null;
        }
        if (clazz == Integer.class || clazz == int.class) {
            return NumberUtils.isCreatable(value.asText());
        }
        if (clazz == Long.class || clazz == long.class) {
            return NumberUtils.isCreatable(value.asText());
        }
        if (clazz == Double.class || clazz == double.class || clazz == Float.class || clazz == float.class) {
            return NumberUtils.isCreatable(value.asText());
        }
        if (clazz == String.class) {
            return StringUtils.hasText(value.asText());
        }
        if (clazz == LocalDate.class || clazz == LocalDateTime.class) {
            return ConvertUtil.isDateValue(value.asText());
        }
        return false;
    }

    private String getLabel(UiViewAttribute attribute) {
        return JsonUtil.getLocalizedMessage(messageSource, attribute.getLabel());
    }
}
