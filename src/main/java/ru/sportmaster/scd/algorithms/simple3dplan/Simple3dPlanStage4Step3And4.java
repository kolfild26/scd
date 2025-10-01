package ru.sportmaster.scd.algorithms.simple3dplan;

import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamNames.SIMPLE_3D_PLAN_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.SIMPLE_3D_PLAN_SKIP_OUTLIER_PARAMS_DEFINITIONS;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * Расчет 3D-Simple - плана продаж для ЦП (Спортмастер) (3D plan simple SCD). Этап 4. Шаги 3 и 4.
 */
@Component
public class Simple3dPlanStage4Step3And4 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "3D_PLAN_SIMPLE_STAGE4_STEP3_4";
    private static final String EXECUTOR_DESCRIPTION =
        "Опреденеие \"узлов-выбросов\" в распределении 3D-плана && Проверка целостности... (Этап 4. Шаги 3 и 4.)";

    public Simple3dPlanStage4Step3And4() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(SIMPLE_3D_PLAN_PACKAGE_NAME)
                .procedureName("stage_4_step_3_4")
                .paramDefinitions(SIMPLE_3D_PLAN_SKIP_OUTLIER_PARAMS_DEFINITIONS)
                .build()
        );
    }
}
