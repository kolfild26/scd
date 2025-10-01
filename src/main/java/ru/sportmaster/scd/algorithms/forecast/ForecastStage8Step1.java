package ru.sportmaster.scd.algorithms.forecast;

import static ru.sportmaster.scd.consts.ParamNames.FORECAST_MATRIX_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.PARAMS_DEFINITIONS;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * Спуск прогноза и передача остальных данных из SCD для алгоритмов 1 и 2-го прогонов. Этап 8. Шаг 1.
 */
@Component
public class ForecastStage8Step1 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "FORECAST_STAGE8_STEP1";
    private static final String EXECUTOR_DESCRIPTION =
        "Спуск прогноза и отдача его в алгоритмы 1-го и 2-го прогонов (Этап 8. Шаг 1.)";

    public ForecastStage8Step1() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(FORECAST_MATRIX_PACKAGE_NAME)
                .procedureName("stage_8_step_1")
                .paramDefinitions(PARAMS_DEFINITIONS)
                .build()
        );
    }
}
