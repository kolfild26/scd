package ru.sportmaster.scd.service.adjustment.validator;

import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.dto.adjustment.AdjustmentValidationErrorDto;
import ru.sportmaster.scd.dto.adjustment.ValidationError;
import ru.sportmaster.scd.entity.adjustment.AdjustmentType;
import ru.sportmaster.scd.entity.adjustment.CoefficientUpperPlanLimitMSC_;
import ru.sportmaster.scd.entity.adjustment.LcMinUpaDSCS_;
import ru.sportmaster.scd.entity.adjustment.LcMinUpaDSC_;
import ru.sportmaster.scd.entity.adjustment.LcMinUpaMSCS_;
import ru.sportmaster.scd.entity.adjustment.LcMinUpaMSC_;
import ru.sportmaster.scd.entity.adjustment.MinUpaDSCS_;
import ru.sportmaster.scd.entity.adjustment.MinUpaDSC_;
import ru.sportmaster.scd.entity.adjustment.MinUpaMSCS_;
import ru.sportmaster.scd.entity.adjustment.MinUpaMSC_;
import ru.sportmaster.scd.entity.adjustment.Plan3DMtpl_;
import ru.sportmaster.scd.entity.adjustment.Plan3DVal_;
import ru.sportmaster.scd.entity.adjustment.PlanLimitDSC_;
import ru.sportmaster.scd.entity.adjustment.PlanLimitMSC_;
import ru.sportmaster.scd.entity.adjustment.StartListStoreDSCS_;
import ru.sportmaster.scd.entity.adjustment.StartListStoreDSC_;
import ru.sportmaster.scd.entity.adjustment.StartListStoreMSCS_;
import ru.sportmaster.scd.entity.adjustment.StartListStoreMSC_;
import ru.sportmaster.scd.ui.view.type.UiView;
import ru.sportmaster.scd.ui.view.type.UiViewAttribute;
import ru.sportmaster.scd.utils.JsonUtil;

@Component
@RequiredArgsConstructor
public class DocumentPositiveValueValidator implements DocumentRowValidator {
    private final MessageSource messageSource;
    private static final Map<AdjustmentType, String> POSITIVE_VALUES_TYPES = new EnumMap<>(AdjustmentType.class);

    @PostConstruct
    public void init() {
        POSITIVE_VALUES_TYPES.put(AdjustmentType.PLAN_3D_VAL, Plan3DVal_.VALUE);
        POSITIVE_VALUES_TYPES.put(AdjustmentType.PLAN_3D_MTPL, Plan3DMtpl_.MULT);
        POSITIVE_VALUES_TYPES.put(AdjustmentType.PLAN_LIMIT_MSC, PlanLimitMSC_.VALUE);
        POSITIVE_VALUES_TYPES.put(AdjustmentType.COEFFICIENT_UPPER_PLAN_LIMIT_MSC, CoefficientUpperPlanLimitMSC_.VALUE);
        POSITIVE_VALUES_TYPES.put(AdjustmentType.PLAN_LIMIT_DSC, PlanLimitDSC_.VALUE);
        POSITIVE_VALUES_TYPES.put(AdjustmentType.COEFFICIENT_UPPER_PLAN_LIMIT_DSC, Plan3DMtpl_.MULT);
        POSITIVE_VALUES_TYPES.put(AdjustmentType.MIN_UPA_MSC, MinUpaMSC_.UPA);
        POSITIVE_VALUES_TYPES.put(AdjustmentType.MIN_UPA_DSC, MinUpaDSC_.UPA);
        POSITIVE_VALUES_TYPES.put(AdjustmentType.MIN_UPA_MSCS, MinUpaMSCS_.UPA);
        POSITIVE_VALUES_TYPES.put(AdjustmentType.MIN_UPA_DSCS, MinUpaDSCS_.UPA);
        POSITIVE_VALUES_TYPES.put(AdjustmentType.LC_MIN_UPA_MSC, LcMinUpaMSC_.VALUE);
        POSITIVE_VALUES_TYPES.put(AdjustmentType.LC_MIN_UPA_DSC, LcMinUpaDSC_.VALUE);
        POSITIVE_VALUES_TYPES.put(AdjustmentType.LC_MIN_UPA_MSCS, LcMinUpaMSCS_.VALUE);
        POSITIVE_VALUES_TYPES.put(AdjustmentType.LC_MIN_UPA_DSCS, LcMinUpaDSCS_.VALUE);
        POSITIVE_VALUES_TYPES.put(AdjustmentType.START_LIST_STORE_MSCS, StartListStoreMSCS_.MIN_UPA);
        POSITIVE_VALUES_TYPES.put(AdjustmentType.START_LIST_STORE_MSC, StartListStoreMSC_.MIN_UPA);
        POSITIVE_VALUES_TYPES.put(AdjustmentType.START_LIST_STORE_DSCS, StartListStoreDSCS_.MIN_UPA);
        POSITIVE_VALUES_TYPES.put(AdjustmentType.START_LIST_STORE_DSC, StartListStoreDSC_.MIN_UPA);
    }

    @Override
    public boolean isSupported(AdjustmentType type) {
        return POSITIVE_VALUES_TYPES.containsKey(type);
    }

    @Override
    public List<AdjustmentValidationErrorDto> validate(UiView view, ObjectNode node, long index) {
        var errors = new ArrayList<AdjustmentValidationErrorDto>();
        var attributes = view.getAttributes();
        var adjustmentType = AdjustmentType.findByClass(view.getJavaType());
        var valueField = POSITIVE_VALUES_TYPES.get(adjustmentType);

        for (int i = 0; i < attributes.size(); i++) {
            var attribute = attributes.get(i);
            var label = getLabel(attribute);

            if (valueField.equals(attribute.getName()) && node.has(label)) {
                var value = node.get(label).doubleValue();
                if (Double.compare(value, 0.0) < 0) {
                    errors.add(buildError(index, i, attribute));
                }
            }
        }

        return errors;
    }

    private AdjustmentValidationErrorDto buildError(long rowIndex, long colIndex, UiViewAttribute attribute) {
        return AdjustmentValidationErrorDto.builder()
            .rowIndex(rowIndex - 1)
            .colIndex(colIndex)
            .key(ValidationError.ONLY_POSITIVE_VALUE)
            .field(getLabel(attribute))
            .build();
    }

    private String getLabel(UiViewAttribute attribute) {
        return JsonUtil.getLocalizedMessage(messageSource, attribute.getLabel());
    }
}
