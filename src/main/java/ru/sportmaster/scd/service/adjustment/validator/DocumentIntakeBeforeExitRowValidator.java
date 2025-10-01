package ru.sportmaster.scd.service.adjustment.validator;

import static ru.sportmaster.scd.utils.ConvertUtil.getDateValue;
import static ru.sportmaster.scd.utils.ConvertUtil.isDateValue;
import static ru.sportmaster.scd.utils.JsonUtil.getColumnIndex;

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
import ru.sportmaster.scd.utils.JsonUtil;

@Component
@RequiredArgsConstructor
public class DocumentIntakeBeforeExitRowValidator implements DocumentRowValidator {
    private static final List<AdjustmentType> SUPPORTED_TYPES = List.of(
        AdjustmentType.INTAKE_EXIT_DATE_MSC,
        AdjustmentType.INTAKE_EXIT_DATE_DSC
    );

    private final MessageSource messageSource;
    private String intakeDateLabel;
    private String exitDateLabel;

    @PostConstruct
    public void init() {
        intakeDateLabel = JsonUtil.getLocalizedMessage(messageSource, "intakeDate");
        exitDateLabel = JsonUtil.getLocalizedMessage(messageSource, "exitDate");
    }

    @Override
    public boolean isSupported(AdjustmentType type) {
        return SUPPORTED_TYPES.contains(type);
    }

    @Override
    public List<AdjustmentValidationErrorDto> validate(UiView view, ObjectNode node, long index) {
        List<AdjustmentValidationErrorDto> errors = new ArrayList<>();
        LocalDate intakeDate = convertDate(node, intakeDateLabel);
        LocalDate exitDate = convertDate(node, exitDateLabel);

        if (intakeDate != null && exitDate != null && !intakeDate.isBefore(exitDate)) {
            errors.add(AdjustmentValidationErrorDto.builder()
                .rowIndex(index - 1)
                .colIndex(getColumnIndex(node, intakeDateLabel))
                .key(ValidationError.INTAKE_AFTER_EXIT_DATE)
                .field(intakeDateLabel)
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
