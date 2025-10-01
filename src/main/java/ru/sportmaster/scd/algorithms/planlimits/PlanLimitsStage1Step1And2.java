package ru.sportmaster.scd.algorithms.planlimits;

import static ru.sportmaster.scd.consts.ParamNames.PLAN_LIMITS_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.PARAMS_DEFINITIONS_WITH_CALC_DATE;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * Расчет планового лимита (СМ, розница). Этап 1. Шаги 1 и 2.
 */
@Component
public class PlanLimitsStage1Step1And2 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "PLAN_LIMITS_STAGE1_STEP1_2";
    private static final String EXECUTOR_DESCRIPTION =
        "Подготовка срезов ассортиментной матрицы и матрицы презентации && "
            + "Определение объема товара для распределения в штуках (Volume_u) (Этап 1. Шаги 1 и 2.)";

    public PlanLimitsStage1Step1And2() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(PLAN_LIMITS_PACKAGE_NAME)
                .procedureName("stage_1_step_1_2")
                .paramDefinitions(PARAMS_DEFINITIONS_WITH_CALC_DATE)
                .build()
        );
    }
}
