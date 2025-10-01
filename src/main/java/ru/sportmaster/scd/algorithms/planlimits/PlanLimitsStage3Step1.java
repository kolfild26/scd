package ru.sportmaster.scd.algorithms.planlimits;

import static ru.sportmaster.scd.consts.ParamNames.PLAN_LIMITS_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.PARAMS_DEFINITIONS;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * Расчет планового лимита (СМ, розница). Этап 3. Шаг 1.
 */
@Component
public class PlanLimitsStage3Step1 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "PLAN_LIMITS_STAGE3_STEP1";
    private static final String EXECUTOR_DESCRIPTION =
        "Фиксация планового лимита (Этап 3. Шаг 1.)";

    public PlanLimitsStage3Step1() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(PLAN_LIMITS_PACKAGE_NAME)
                .procedureName("stage_3_step_1")
                .paramDefinitions(PARAMS_DEFINITIONS)
                .build()
        );
    }
}
