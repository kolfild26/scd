package ru.sportmaster.scd.algorithms.commmatrixm;

import static ru.sportmaster.scd.consts.ParamNames.COMMODITY_MATRIX_M_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.FULL_PARAMS_DEFINITIONS;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * Формирование многоинтервальной товарной матрицы SCD (Спортмастер + Остин) Этап 1. Шаг 1.
 */
@Component
public class CommodityMatrixMStage1Step1 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "COMMODITY_MATRIX_M_STAGE1_STEP1";
    private static final String EXECUTOR_DESCRIPTION =
        "Формирование коллекционной матрицы квотирования на уровне РЦМ / магазин (Этап 1. Шаг 1.)";

    public CommodityMatrixMStage1Step1() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(COMMODITY_MATRIX_M_PACKAGE_NAME)
                .procedureName("stage_1_step_1")
                .paramDefinitions(FULL_PARAMS_DEFINITIONS)
                .build()
        );
    }
}
