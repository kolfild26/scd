package ru.sportmaster.scd.service.adjustment.validator;

import static ru.sportmaster.scd.utils.ConvertUtil.getDateValue;
import static ru.sportmaster.scd.utils.ConvertUtil.isDateValue;
import static ru.sportmaster.scd.utils.JsonUtil.getColumnIndex;
import static ru.sportmaster.scd.utils.JsonUtil.getLocalizedMessage;

import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.dto.adjustment.AdjustmentValidationErrorDto;
import ru.sportmaster.scd.dto.adjustment.ValidationError;
import ru.sportmaster.scd.entity.adjustment.AdjustmentType;
import ru.sportmaster.scd.ui.view.type.UiView;

@Component
@RequiredArgsConstructor
public class DocumentReplStrategyStartEndDatesValidator implements DocumentRowValidator {
    private static final List<AdjustmentType> SUPPORTED_TYPES = List.of(
        AdjustmentType.REPLENISHMENT_STRATEGY_MSC,
        AdjustmentType.REPLENISHMENT_STRATEGY_DSC
    );

    private final MessageSource messageSource;
    private String startDayLabel;
    private String endDayLabel;

    @PostConstruct
    public void init() {
        startDayLabel = getLocalizedMessage(messageSource, "startDay");
        endDayLabel = getLocalizedMessage(messageSource, "endDay");
    }

    @Override
    public boolean isSupported(AdjustmentType type) {
        return SUPPORTED_TYPES.contains(type);
    }

    @Override
    public List<AdjustmentValidationErrorDto> validate(UiView view, ObjectNode node, long index) {
        List<AdjustmentValidationErrorDto> errors = new ArrayList<>();
        LocalDate startDay = convertDate(node, startDayLabel);
        LocalDate endDay = convertDate(node, endDayLabel);

        if (startDay != null && startDay.getDayOfWeek().ordinal() != 0) {
            errors.add(AdjustmentValidationErrorDto.builder()
                .rowIndex(index - 1)
                .colIndex(getColumnIndex(node, startDayLabel))
                .key(ValidationError.REPL_PERIOD_MULT_WEEK)
                .field(startDayLabel)
                .build());
        }

        if (endDay != null && endDay.getDayOfWeek().ordinal() != 6) {
            errors.add(AdjustmentValidationErrorDto.builder()
                .rowIndex(index - 1)
                .colIndex(getColumnIndex(node, endDayLabel))
                .key(ValidationError.REPL_PERIOD_MULT_WEEK)
                .field(endDayLabel)
                .build());
        }

        if (startDay != null && endDay != null && !startDay.isBefore(endDay)) {
            errors.add(AdjustmentValidationErrorDto.builder()
                .rowIndex(index - 1)
                .colIndex(getColumnIndex(node, endDayLabel))
                .key(ValidationError.REPL_START_END_DATE)
                .field(endDayLabel)
                .build());
        }

        return errors;
    }

    private LocalDate convertDate(ObjectNode node, String path) {
        if (node == null || !node.hasNonNull(path)) {
            return null;
        }
        String value = node.get(path).asText();
        return isDateValue(value) ? getDateValue(value) : null;
    }
}
