package ru.sportmaster.scd.algorithms.replstrategyinseason;

import static ru.sportmaster.scd.consts.ParamNames.REPLENISHMENT_STRATEGY_INSEASON_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.SIMPLE_3D_PLAN_CALC_DATE_PARAMS_DEFINITIONS;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * InSeason: Стратегии пополнения. Этап 4. Шаг 2.
 */
@Component
public class ReplenishmentStrategyInseasonStage4Step2 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "REPL_STRATEGY_INSEASON_STAGE4_STEP2";
    private static final String EXECUTOR_DESCRIPTION =
        "Ручное переопределение стратегий пополнения InSeason";

    public ReplenishmentStrategyInseasonStage4Step2() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(REPLENISHMENT_STRATEGY_INSEASON_PACKAGE_NAME)
                .procedureName("stage_4_step_2")
                .paramDefinitions(SIMPLE_3D_PLAN_CALC_DATE_PARAMS_DEFINITIONS)
                .build()
        );
    }
}
