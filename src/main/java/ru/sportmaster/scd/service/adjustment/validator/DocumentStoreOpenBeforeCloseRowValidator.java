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
public class DocumentStoreOpenBeforeCloseRowValidator implements DocumentRowValidator {
    private final MessageSource messageSource;
    private String storeOpenDate;
    private String storeCloseDate;

    @PostConstruct
    public void init() {
        storeOpenDate = JsonUtil.getLocalizedMessage(messageSource, "manualOpenDate");
        storeCloseDate = JsonUtil.getLocalizedMessage(messageSource, "manualCloseDate");
    }

    @Override
    public boolean isSupported(AdjustmentType type) {
        return AdjustmentType.STORE_OPEN_STORE_CLOSE_DATE == type;
    }

    @Override
    public List<AdjustmentValidationErrorDto> validate(UiView view, ObjectNode node, long index) {
        List<AdjustmentValidationErrorDto> errors = new ArrayList<>();
        LocalDate openDate = convertDate(node, storeOpenDate);
        LocalDate closeDate = convertDate(node, storeCloseDate);

        if (openDate != null && closeDate != null && !openDate.isBefore(closeDate)) {
            errors.add(AdjustmentValidationErrorDto.builder()
                .rowIndex(index - 1)
                .colIndex(getColumnIndex(node, storeOpenDate))
                .key(ValidationError.STORE_OPEN_AFTER_CLOSE_DATE)
                .field(storeOpenDate)
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
