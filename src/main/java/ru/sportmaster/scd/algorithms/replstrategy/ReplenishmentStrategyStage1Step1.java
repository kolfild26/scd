package ru.sportmaster.scd.algorithms.replstrategy;

import static ru.sportmaster.scd.consts.ParamNames.REPLENISHMENT_STRATEGY_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.FULL_PARAMS_DEFINITIONS;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * Стратегии пополнения. Этап 1. Шаг 1.
 */
@Component
public class ReplenishmentStrategyStage1Step1 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "REPL_STRATEGY_STAGE1_STEP1";
    private static final String EXECUTOR_DESCRIPTION =
        "Подготовка ассортиментной матрицы, плановых лимитов и презентации к дальнейшему расчёту (Этап 1. Шаг 1.)";

    public ReplenishmentStrategyStage1Step1() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(REPLENISHMENT_STRATEGY_PACKAGE_NAME)
                .procedureName("stage_1_step_1")
                .paramDefinitions(FULL_PARAMS_DEFINITIONS)
                .build()
        );
    }
}
