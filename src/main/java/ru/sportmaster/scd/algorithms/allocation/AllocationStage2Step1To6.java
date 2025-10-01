package ru.sportmaster.scd.algorithms.allocation;

import static ru.sportmaster.scd.consts.ParamNames.ALLOCATION_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.ALLOCATION_PARAMS_DEFINITIONS;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * Расчет страхового запаса продающих узлов для первой поставки (Allocation SCD). Этап 2. Шаги с 1 по 6.
 */
@Component
public class AllocationStage2Step1To6 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "ALLOCATION_STAGE2_STEP1_6";
    private static final String EXECUTOR_DESCRIPTION =
        "Проверка согласованности показателей страхового запаса... (Этап 2. Шаги с 1 по 6.)";

    public AllocationStage2Step1To6() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(ALLOCATION_PACKAGE_NAME)
                .procedureName("stage_2")
                .paramDefinitions(ALLOCATION_PARAMS_DEFINITIONS)
                .build()
        );
    }
}
