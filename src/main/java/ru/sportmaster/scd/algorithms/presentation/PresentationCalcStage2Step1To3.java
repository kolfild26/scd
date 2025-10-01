package ru.sportmaster.scd.algorithms.presentation;

import static ru.sportmaster.scd.consts.ParamNames.PRESENTATION_CALC_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.PARAMS_DEFINITIONS;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * Расчет презентации на уровне ЦР. Этап 2. Шаги 1, 2 и 3.
 */
@Component
public class PresentationCalcStage2Step1To3 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "PRESENTATION_CALC_STAGE2_STEP1_3";
    private static final String EXECUTOR_DESCRIPTION =
        "Определение веса каждого ЦР в ЦМ && Расчет предварительного значения && Приведение к равенству "
            + "(Этап 2. Шаги 1, 2 и 3.)";

    public PresentationCalcStage2Step1To3() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(PRESENTATION_CALC_PACKAGE_NAME)
                .procedureName("stage_2_step_1_3")
                .paramDefinitions(PARAMS_DEFINITIONS)
                .build()
        );
    }
}
