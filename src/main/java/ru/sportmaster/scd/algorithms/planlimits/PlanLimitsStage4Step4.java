package ru.sportmaster.scd.algorithms.planlimits;

import static ru.sportmaster.scd.consts.ParamNames.PLAN_LIMITS_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.PARAMS_DEFINITIONS;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * Расчет планового лимита (СМ, розница). Этап 4. Шаг 4.
 */
@Component
public class PlanLimitsStage4Step4 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "PLAN_LIMITS_STAGE4_STEP4";
    private static final String EXECUTOR_DESCRIPTION =
        "Проверка достаточности товара для удовлетворения правилу презентации (Этап 4. Шаг 4.)";

    public PlanLimitsStage4Step4() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(PLAN_LIMITS_PACKAGE_NAME)
                .procedureName("stage_4_step_4")
                .paramDefinitions(PARAMS_DEFINITIONS)
                .build()
        );
    }
}
