package ru.sportmaster.scd.algorithms.planlimitsinseason;

import static ru.sportmaster.scd.consts.ParamNames.PLAN_LIMITS_INSEASON_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.SIMPLE_3D_PLAN_SIMPLE_PARAMS_DEFINITIONS;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * InSeason: Плановые лимиты. Этап 3. Шаг 1.
 */
@Component
public class PlanLimitsInseasonStage3Step1 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "PLAN_LIMITS_INSEASON_STAGE3_STEP1";
    private static final String EXECUTOR_DESCRIPTION =
        "Расчет распределенного на узел остатка товара (Этап 3. Шаг 1.)";

    public PlanLimitsInseasonStage3Step1() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(PLAN_LIMITS_INSEASON_PACKAGE_NAME)
                .procedureName("stage_3_step_1")
                .paramDefinitions(SIMPLE_3D_PLAN_SIMPLE_PARAMS_DEFINITIONS)
                .build()
        );
    }
}
