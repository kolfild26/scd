package ru.sportmaster.scd.algorithms.replstrategy;

import static ru.sportmaster.scd.consts.ParamNames.REPLENISHMENT_STRATEGY_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.PARAMS_DEFINITIONS;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * Стратегии пополнения. Этап 3. Шаг 1.
 */
@Component
public class ReplenishmentStrategyStage3Step1 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "REPL_STRATEGY_STAGE3_STEP1";
    private static final String EXECUTOR_DESCRIPTION =
        "Назначение цепочки рекомендованных стратегий пополнения цветомодель-магазинам (Этап 3. Шаг 1.)";

    public ReplenishmentStrategyStage3Step1() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(REPLENISHMENT_STRATEGY_PACKAGE_NAME)
                .procedureName("stage_3_step_1")
                .paramDefinitions(PARAMS_DEFINITIONS)
                .build()
        );
    }
}
