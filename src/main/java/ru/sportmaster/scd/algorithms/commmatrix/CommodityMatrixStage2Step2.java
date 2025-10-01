package ru.sportmaster.scd.algorithms.commmatrix;

import static ru.sportmaster.scd.consts.ParamNames.COMMODITY_MATRIX_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.PARAMS_DEFINITIONS;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * Формирование товарной матрицы в SCD. Этап 2. Шаг 2.
 */
@Component
public class CommodityMatrixStage2Step2 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "COMMODITY_MATRIX_STAGE2_STEP2";
    private static final String EXECUTOR_DESCRIPTION = "Объединение дублей квотирования (Этап 2. Шаг 2.)";

    public CommodityMatrixStage2Step2() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(COMMODITY_MATRIX_PACKAGE_NAME)
                .procedureName("stage_2_step_2")
                .paramDefinitions(PARAMS_DEFINITIONS)
                .build()
        );
    }
}
