package ru.sportmaster.scd.algorithms.presentation;

import static ru.sportmaster.scd.consts.ParamNames.PRESENTATION_CALC_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.FULL_PARAMS_DEFINITIONS;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * Переход к безколлекционному представлению. Этап 3. Шаг 2.
 */
@Component
public class PresentationCalcStage3Step2 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "PRESENTATION_CALC_STAGE3_STEP2";
    private static final String EXECUTOR_DESCRIPTION = "Переход к безколлекционному представлению (Этап 3. Шаг 2.)";

    public PresentationCalcStage3Step2() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(PRESENTATION_CALC_PACKAGE_NAME)
                .procedureName("stage_3_step_2")
                .paramDefinitions(FULL_PARAMS_DEFINITIONS)
                .build()
        );
    }
}
