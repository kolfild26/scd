package ru.sportmaster.scd.algorithms.planlimits;

import static ru.sportmaster.scd.consts.ParamNames.PLAN_LIMITS_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.PARAMS_DEFINITIONS;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * Расчет планового лимита (СМ, розница). Этап 2. Шаг 4.
 */
@Component
public class PlanLimitsStage2Step4 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "PLAN_LIMITS_STAGE2_STEP4";
    private static final String EXECUTOR_DESCRIPTION =
        "Формирование итоговой матрицы фиксации планового лимита (Этап 2. Шаг 4.)";

    public PlanLimitsStage2Step4() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(PLAN_LIMITS_PACKAGE_NAME)
                .procedureName("stage_2_step_4")
                .paramDefinitions(PARAMS_DEFINITIONS)
                .build()
        );
    }
}
