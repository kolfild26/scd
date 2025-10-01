package ru.sportmaster.scd.algorithms.simple3dplan;

import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamNames.SIMPLE_3D_PLAN_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.SIMPLE_3D_PLAN_PARAMS_DEFINITIONS;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * Расчет 3D-Simple - плана продаж для ЦП (Спортмастер) (3D plan simple SCD). Этап 1. Шаг 5.
 */
@Component
public class Simple3dPlanStage1Step5 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "3D_PLAN_SIMPLE_STAGE1_STEP5";
    private static final String EXECUTOR_DESCRIPTION =
        "Квотирование строк планирования (Этап 1. Шаг 5.)";

    public Simple3dPlanStage1Step5() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(SIMPLE_3D_PLAN_PACKAGE_NAME)
                .procedureName("stage_1_step_5")
                .paramDefinitions(SIMPLE_3D_PLAN_PARAMS_DEFINITIONS)
                .build()
        );
    }
}
