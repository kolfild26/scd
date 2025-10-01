package ru.sportmaster.scd.algorithms.forecast;

import static ru.sportmaster.scd.consts.ParamNames.FORECAST_MATRIX_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.PARAMS_DEFINITIONS;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * Спуск прогноза и передача остальных данных из SCD для алгоритмов 1 и 2-го прогонов. Этап 5. Шаг 8.
 */
@Component
public class ForecastStage5Step8 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "FORECAST_STAGE5_STEP8";
    private static final String EXECUTOR_DESCRIPTION =
        "Построение Шкалы ранжирования значений прогноза, подлежащих разнесению (Этап 5. Шаг 8.)";

    public ForecastStage5Step8() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(FORECAST_MATRIX_PACKAGE_NAME)
                .procedureName("stage_5_step_8")
                .paramDefinitions(PARAMS_DEFINITIONS)
                .build()
        );
    }
}
