package ru.sportmaster.scd.algorithms.algprep;

import static ru.sportmaster.scd.consts.ParamNames.ALLOCATION_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.ALLOCATION_PREP_PARAMS_DEFINITIONS;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * Расчет страхового запаса продающих узлов для первой поставки. 
 * Подготовка данных для расчета страхового запаса (Allocation SCD). Этап 1. Шаги 1.
 */
@Component
public class AllocationPrepStage1Step1 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "ALLOCATION_PREP_STAGE1_STEP1";
    private static final String EXECUTOR_DESCRIPTION =
        "Формирование таблиц назначения показателей страховых запасов на уровне ЦМ / магазин... (Этап 1. Шаг 1.)";

    public AllocationPrepStage1Step1() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(ALLOCATION_PACKAGE_NAME)
                .procedureName("prepare")
                .paramDefinitions(ALLOCATION_PREP_PARAMS_DEFINITIONS)
                .build()
        );
    }
}
