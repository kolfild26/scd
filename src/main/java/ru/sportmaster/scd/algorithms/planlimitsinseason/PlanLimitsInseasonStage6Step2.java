package ru.sportmaster.scd.algorithms.planlimitsinseason;

import static ru.sportmaster.scd.consts.ParamNames.PLAN_LIMITS_INSEASON_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.SIMPLE_3D_PLAN_CALC_DATE_PARAMS_DEFINITIONS;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * InSeason: Плановые лимиты. Этап 6. Шаг 2.
 */
@Component
public class PlanLimitsInseasonStage6Step2 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "PLAN_LIMITS_INSEASON_STAGE6_STEP2";
    private static final String EXECUTOR_DESCRIPTION =
        "Формирование сводной таблицы плановых лимитов In/Pre-Season для выгрузки в сторонние системы (Этап 6. Шаг 2.)";

    public PlanLimitsInseasonStage6Step2() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(PLAN_LIMITS_INSEASON_PACKAGE_NAME)
                .procedureName("stage_6_step_2")
                .paramDefinitions(SIMPLE_3D_PLAN_CALC_DATE_PARAMS_DEFINITIONS)
                .build()
        );
    }
}
