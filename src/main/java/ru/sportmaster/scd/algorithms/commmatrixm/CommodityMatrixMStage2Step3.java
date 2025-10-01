package ru.sportmaster.scd.algorithms.commmatrixm;

import static ru.sportmaster.scd.consts.ParamNames.COMMODITY_MATRIX_M_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.PARAMS_DEFINITIONS;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * Формирование многоинтервальной товарной матрицы SCD (Спортмастер + Остин). Этап 2. Шаг 3.
 */
@Component
public class CommodityMatrixMStage2Step3 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "COMMODITY_MATRIX_M_STAGE2_STEP3";
    private static final String EXECUTOR_DESCRIPTION =
        "Обработка исключений стоп-листа на уровне ЦР / магазин (Этап 2. Шаг 3.)";

    public CommodityMatrixMStage2Step3() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(COMMODITY_MATRIX_M_PACKAGE_NAME)
                .procedureName("stage_2_step_3")
                .paramDefinitions(PARAMS_DEFINITIONS)
                .build()
        );
    }
}
