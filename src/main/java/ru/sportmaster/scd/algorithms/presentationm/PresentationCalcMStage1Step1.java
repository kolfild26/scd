package ru.sportmaster.scd.algorithms.presentationm;

import static ru.sportmaster.scd.consts.ParamNames.PRESENTATION_CALC_M_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.PARAMS_DEFINITIONS;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * Формирование многоинтервальной презентации (СМ + Остин). Этап 1. Шаг 1.
 */
@Component
public class PresentationCalcMStage1Step1 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "PRESENTATION_CALC_M_STAGE1_STEP1";
    private static final String EXECUTOR_DESCRIPTION =
        "Формирование таблицы презентации на уровне ЦР / магазин / период ЖЦ (Presentation_SCS_ST) (Этап 1. Шаг 1.)";

    public PresentationCalcMStage1Step1() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(PRESENTATION_CALC_M_PACKAGE_NAME)
                .procedureName("stage_1_step_1")
                .paramDefinitions(PARAMS_DEFINITIONS)
                .build()
        );
    }
}
