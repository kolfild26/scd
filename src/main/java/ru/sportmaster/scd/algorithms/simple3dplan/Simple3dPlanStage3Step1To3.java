package ru.sportmaster.scd.algorithms.simple3dplan;

import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamNames.SIMPLE_3D_PLAN_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.SIMPLE_3D_PLAN_PARAMS_DEFINITIONS;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * Расчет 3D-Simple - плана продаж для ЦП (Спортмастер) (3D plan simple SCD). Этап 3. Шаги 1, 2 и 3.
 */
@Component
public class Simple3dPlanStage3Step1To3 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "3D_PLAN_SIMPLE_STAGE3_STEP1_3";
    private static final String EXECUTOR_DESCRIPTION =
        "Применение прототипов && Коррекция распределения на плановый оборот TY (Этап 3. Шаги 1, 2 и 3.)";

    public Simple3dPlanStage3Step1To3() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(SIMPLE_3D_PLAN_PACKAGE_NAME)
                .procedureName("stage_3_step_1_3")
                .paramDefinitions(SIMPLE_3D_PLAN_PARAMS_DEFINITIONS)
                .build()
        );
    }
}
