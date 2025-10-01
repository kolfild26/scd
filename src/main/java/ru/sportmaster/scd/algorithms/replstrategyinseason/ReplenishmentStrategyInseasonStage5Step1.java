package ru.sportmaster.scd.algorithms.replstrategyinseason;

import static ru.sportmaster.scd.consts.ParamNames.REPLENISHMENT_STRATEGY_INSEASON_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.SIMPLE_3D_PLAN_CALC_DATE_PARAMS_DEFINITIONS;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * InSeason: Стратегии пополнения. Этап 5. Шаг 1.
 */
@Component
public class ReplenishmentStrategyInseasonStage5Step1 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "REPL_STRATEGY_INSEASON_STAGE5_STEP1";
    private static final String EXECUTOR_DESCRIPTION =
        "Склейка данных стратегий пополнения PreSeason и InSeason";

    public ReplenishmentStrategyInseasonStage5Step1() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(REPLENISHMENT_STRATEGY_INSEASON_PACKAGE_NAME)
                .procedureName("stage_5_step_1")
                .paramDefinitions(SIMPLE_3D_PLAN_CALC_DATE_PARAMS_DEFINITIONS)
                .build()
        );
    }
}
