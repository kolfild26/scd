package ru.sportmaster.scd.algorithms.forecastprep;

import static ru.sportmaster.scd.consts.ParamNames.FORECAST_PREP_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.PARAMS_DEFINITIONS_PREP;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * Подготовительные действия для алгоритма Спуска прогноза. Этап 2. Шаг 2.
 */
@Component
public class ForecastPrepStage2Step2 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "FORECAST_PREP_STAGE2_STEP2";
    private static final String EXECUTOR_DESCRIPTION =
        "Переход к OMNI и MP продажам цветоразмеров разработки (Этап 2. Шаг 2.)";

    public ForecastPrepStage2Step2() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(FORECAST_PREP_PACKAGE_NAME)
                .procedureName("stage_3_step_2")
                .paramDefinitions(PARAMS_DEFINITIONS_PREP)
                .build()
        );
    }
}
