package ru.sportmaster.scd.algorithms.rollup;

import static ru.sportmaster.scd.consts.ParamNames.ROLLUP_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.ALLOCATION_PARAMS_DEFINITIONS;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * РСвёртки данных по остаткам. Этап 1. Шаг 1.
 */
@Component
public class RollupStage1Step1 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "ROLLUP_STAGE1_STEP1";
    private static final String EXECUTOR_DESCRIPTION =
        "Свёртка детализированного по потокам прогнозного остатка мерчцветоразмеров на интервалах действия"
            + " (Этап 1. Шаг 1.)";

    public RollupStage1Step1() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(ROLLUP_PACKAGE_NAME)
                .procedureName("stage_1_step_1")
                .paramDefinitions(ALLOCATION_PARAMS_DEFINITIONS)
                .build()
        );
    }
}
