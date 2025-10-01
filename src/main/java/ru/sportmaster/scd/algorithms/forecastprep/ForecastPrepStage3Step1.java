package ru.sportmaster.scd.algorithms.forecastprep;

import static ru.sportmaster.scd.consts.ParamNames.FORECAST_PREP_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.PARAMS_DEFINITIONS_PREP;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * Подготовительные действия для алгоритма Спуска прогноза. Этап 3. Шаг 1.
 */
@Component
public class ForecastPrepStage3Step1 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "FORECAST_PREP_STAGE3_STEP1";
    private static final String EXECUTOR_DESCRIPTION =
        "Объединение данных по продажам (Этап 3. Шаг 1.)";

    public ForecastPrepStage3Step1() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(FORECAST_PREP_PACKAGE_NAME)
                .procedureName("stage_4_step_1")
                .paramDefinitions(PARAMS_DEFINITIONS_PREP)
                .build()
        );
    }
}
