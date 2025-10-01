package ru.sportmaster.scd.algorithms.algprep;

import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamNames.SIMPLE_3D_PLAN_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.SIMPLE_3D_PLAN_PREPARE_PARAMS_DEFINITIONS;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * Расчет 3D-Simple - плана продаж для ЦП (Спортмастер).
 * Предварительный шаг. Заполнение расчетных таблиц. (3D plan simple SCD). Этап 2. Шаги 1.
 */
@Component
public class Simple3dPlanPrepStage2Step1 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "3D_PLAN_SIMPLE_PREP_STAGE2_STEP1";
    private static final String EXECUTOR_DESCRIPTION =
        "Предварительный шаг. Заполнение расчетных таблиц. (3D plan simple SCD).. (Этап 2. Шаг 1.)";

    public Simple3dPlanPrepStage2Step1() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(SIMPLE_3D_PLAN_PACKAGE_NAME)
                .procedureName("prepare_calc_data")
                .paramDefinitions(SIMPLE_3D_PLAN_PREPARE_PARAMS_DEFINITIONS)
                .build()
        );
    }
}
