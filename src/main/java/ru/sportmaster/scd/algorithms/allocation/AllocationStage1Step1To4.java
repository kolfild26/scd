package ru.sportmaster.scd.algorithms.allocation;

import static ru.sportmaster.scd.consts.ParamNames.ALLOCATION_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.ALLOCATION_PARAMS_DEFINITIONS;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * Расчет страхового запаса продающих узлов для первой поставки (Allocation SCD). Этап 1. Шаги с 1 по 4.
 */
@Component
public class AllocationStage1Step1To4 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "ALLOCATION_STAGE1_STEP1_4";
    private static final String EXECUTOR_DESCRIPTION =
        "Формирование номенклатуры строк Цветоразмер разработки / магазин... (Этап 1. Шаги с 1 по 4.)";

    public AllocationStage1Step1To4() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(ALLOCATION_PACKAGE_NAME)
                .procedureName("stage_1_step_1_4")
                .paramDefinitions(ALLOCATION_PARAMS_DEFINITIONS)
                .build()
        );
    }
}
