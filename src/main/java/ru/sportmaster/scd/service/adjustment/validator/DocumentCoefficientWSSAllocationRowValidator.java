package ru.sportmaster.scd.service.adjustment.validator;

import static java.util.Objects.isNull;
import static ru.sportmaster.scd.utils.JsonUtil.getColumnIndex;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.dto.adjustment.AdjustmentValidationErrorDto;
import ru.sportmaster.scd.dto.adjustment.ValidationError;
import ru.sportmaster.scd.entity.adjustment.AdjustmentType;
import ru.sportmaster.scd.ui.view.type.UiView;
import ru.sportmaster.scd.utils.JsonUtil;

@Component
@RequiredArgsConstructor
public class DocumentCoefficientWSSAllocationRowValidator implements DocumentRowValidator {
    private static final List<AdjustmentType> SUPPORTED_TYPES = List.of(
        AdjustmentType.MSC_SHOP_WSS_PARAMS,
        AdjustmentType.DSC_SHOP_WSS_PARAMS
    );

    private final MessageSource messageSource;
    private String safetyStockPercentLabel;
    private String minWeekSafetyStockLabel;
    private String maxWeekSafetyStockLabel;
    private String periodWeekSafetyStockLabel;

    @PostConstruct
    public void init() {
        safetyStockPercentLabel = JsonUtil.getLocalizedMessage(messageSource, "safetyStockPercent");
        minWeekSafetyStockLabel = JsonUtil.getLocalizedMessage(messageSource, "minWeekSafetyStock");
        maxWeekSafetyStockLabel = JsonUtil.getLocalizedMessage(messageSource, "maxWeekSafetyStock");
        periodWeekSafetyStockLabel = JsonUtil.getLocalizedMessage(messageSource, "periodWeekSafetyStock");
    }

    @Override
    public boolean isSupported(AdjustmentType type) {
        return SUPPORTED_TYPES.contains(type);
    }

    @Override
    public List<AdjustmentValidationErrorDto> validate(UiView view, ObjectNode node, long index) {
        List<AdjustmentValidationErrorDto> errors = new ArrayList<>();
        var period = convertDouble(node, periodWeekSafetyStockLabel);
        var percent = convertDouble(node, safetyStockPercentLabel);
        var min = convertDouble(node, minWeekSafetyStockLabel);
        var max = convertDouble(node, maxWeekSafetyStockLabel);

        if (isNull(period)) {
            if (isNull(percent)) {
                errors.add(buildError(node, index, safetyStockPercentLabel));
            }
            if (isNull(min)) {
                errors.add(buildError(node, index, minWeekSafetyStockLabel));
            }
            if (isNull(max)) {
                errors.add(buildError(node, index, maxWeekSafetyStockLabel));
            }
        }

        return errors;
    }

    private static AdjustmentValidationErrorDto buildError(ObjectNode node, long index, String label) {
        return AdjustmentValidationErrorDto.builder()
            .rowIndex(index - 1)
            .colIndex(getColumnIndex(node, label))
            .key(ValidationError.COEF_WSS_ALLOCATION)
            .field(label)
            .build();
    }

    private Double convertDouble(ObjectNode node, String path) {
        if (node == null || !node.hasNonNull(path)) {
            return null;
        }
        JsonNode value = node.get(path);
        return value.isNull() ? null : value.asDouble();
    }
}
