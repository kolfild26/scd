package ru.sportmaster.scd.algorithms.replstrategyinseason;

import static ru.sportmaster.scd.consts.ParamNames.REPLENISHMENT_STRATEGY_INSEASON_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.SIMPLE_3D_PLAN_SIMPLE_PARAMS_DEFINITIONS;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * InSeason: Стратегии пополнения. Этап 3. Шаг 2.
 */
@Component
public class ReplenishmentStrategyInseasonStage3Step2 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "REPL_STRATEGY_INSEASON_STAGE3_STEP2";
    private static final String EXECUTOR_DESCRIPTION =
        "Определение выбитости размерного ряда";

    public ReplenishmentStrategyInseasonStage3Step2() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(REPLENISHMENT_STRATEGY_INSEASON_PACKAGE_NAME)
                .procedureName("stage_3_step_2")
                .paramDefinitions(SIMPLE_3D_PLAN_SIMPLE_PARAMS_DEFINITIONS)
                .build()
        );
    }
}
