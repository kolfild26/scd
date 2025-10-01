package ru.sportmaster.scd.algorithms.replstrategy;

import static ru.sportmaster.scd.consts.ParamNames.REPLENISHMENT_STRATEGY_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.PARAMS_DEFINITIONS;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * Стратегии пополнения. Этап 4. Шаг 2.
 */
@Component
public class ReplenishmentStrategyStage4Step2 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "REPL_STRATEGY_STAGE4_STEP2";
    private static final String EXECUTOR_DESCRIPTION =
        "Цепочка финальных стратегий пополнения для цветомодель-ПЛУ (Этап 4. Шаг 2.)";

    public ReplenishmentStrategyStage4Step2() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(REPLENISHMENT_STRATEGY_PACKAGE_NAME)
                .procedureName("stage_4_step_2")
                .paramDefinitions(PARAMS_DEFINITIONS)
                .build()
        );
    }
}
