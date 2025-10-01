package ru.sportmaster.scd.algorithms.planlimits;

import static ru.sportmaster.scd.consts.ParamNames.PLAN_LIMITS_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.PARAMS_DEFINITIONS;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * Расчет планового лимита (СМ, розница). Этап 4. Шаг 1.
 */
@Component
public class PlanLimitsStage4Step1 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "PLAN_LIMITS_STAGE4_STEP1";
    private static final String EXECUTOR_DESCRIPTION =
        "Равномерное распределение объема на ЦР по всем магазинам согласно действующему квотированию (Этап 4. Шаг 1.)";

    public PlanLimitsStage4Step1() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(PLAN_LIMITS_PACKAGE_NAME)
                .procedureName("stage_4_step_1")
                .paramDefinitions(PARAMS_DEFINITIONS)
                .build()
        );
    }
}
