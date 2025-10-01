package ru.sportmaster.scd.algorithms.presentation;

import static ru.sportmaster.scd.consts.ParamNames.PRESENTATION_CALC_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.FULL_PARAMS_DEFINITIONS;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * Актуализация среза коллекционной матрицы. Этап 3. Шаг 1.
 */
@Component
public class PresentationCalcStage3Step1 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "PRESENTATION_CALC_STAGE3_STEP1";
    private static final String EXECUTOR_DESCRIPTION = "Актуализация среза коллекционной матрицы (Этап 3. Шаг 1.)";

    public PresentationCalcStage3Step1() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(PRESENTATION_CALC_PACKAGE_NAME)
                .procedureName("stage_3_step_1")
                .paramDefinitions(FULL_PARAMS_DEFINITIONS)
                .build()
        );
    }
}
