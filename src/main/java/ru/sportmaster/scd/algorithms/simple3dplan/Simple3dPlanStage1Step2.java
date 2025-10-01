package ru.sportmaster.scd.algorithms.simple3dplan;

import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamNames.SIMPLE_3D_PLAN_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.SIMPLE_3D_PLAN_EXTEND_PARAMS_DEFINITIONS;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * Расчет 3D-Simple - плана продаж для ЦП (Спортмастер) (3D plan simple SCD). Этап 1. Шаг 2.
 */
@Component
public class Simple3dPlanStage1Step2 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "3D_PLAN_SIMPLE_STAGE1_STEP2";
    private static final String EXECUTOR_DESCRIPTION = "Преобразование фактов коллекционных продаж (Этап 1. Шаг 2.)";

    public Simple3dPlanStage1Step2() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(SIMPLE_3D_PLAN_PACKAGE_NAME)
                .procedureName("stage_1_step_2")
                .paramDefinitions(SIMPLE_3D_PLAN_EXTEND_PARAMS_DEFINITIONS)
                .build()
        );
    }
}
