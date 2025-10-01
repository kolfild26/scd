package ru.sportmaster.scd.algorithms.forecast;

import static ru.sportmaster.scd.consts.ParamNames.FORECAST_MATRIX_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.FULL_PARAMS_DEFINITIONS;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * Спуск прогноза и передача остальных данных из SCD для алгоритмов 1 и 2-го прогонов. Этап 2. Шаг 3.
 */
@Component
public class ForecastStage2Step3 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "FORECAST_STAGE2_STEP3";
    private static final String EXECUTOR_DESCRIPTION =
        "Отдача ассортиментной матрицы (Этап 2. Шаг 3.)";

    public ForecastStage2Step3() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(FORECAST_MATRIX_PACKAGE_NAME)
                .procedureName("stage_2_step_3")
                .paramDefinitions(FULL_PARAMS_DEFINITIONS)
                .build()
        );
    }
}
