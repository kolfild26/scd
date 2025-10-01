package ru.sportmaster.scd.algorithms.forecast;

import static ru.sportmaster.scd.consts.ParamNames.FORECAST_MATRIX_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.PARAMS_DEFINITIONS;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * Спуск прогноза и передача остальных данных из SCD для алгоритмов 1 и 2-го прогонов. Этап 2. Шаг 4.
 */
@Component
public class ForecastStage2Step4 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "FORECAST_STAGE2_STEP4";
    private static final String EXECUTOR_DESCRIPTION =
        "Отдача плановых лимитов (Этап 2. Шаг 4.)";

    public ForecastStage2Step4() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(FORECAST_MATRIX_PACKAGE_NAME)
                .procedureName("stage_2_step_4")
                .paramDefinitions(PARAMS_DEFINITIONS)
                .build()
        );
    }
}
