package ru.sportmaster.scd.algorithms.planlimits;

import static ru.sportmaster.scd.consts.ParamNames.PLAN_LIMITS_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.PARAMS_DEFINITIONS;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * Расчет планового лимита (СМ, розница). Этап 2. Шаг 2.
 */
@Component
public class PlanLimitsStage2Step2 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "PLAN_LIMITS_STAGE2_STEP2";
    private static final String EXECUTOR_DESCRIPTION =
        "Обработка специфических ситуаций (Этап 2. Шаг 2.)";

    public PlanLimitsStage2Step2() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(PLAN_LIMITS_PACKAGE_NAME)
                .procedureName("stage_2_step_2")
                .paramDefinitions(PARAMS_DEFINITIONS)
                .build()
        );
    }
}
