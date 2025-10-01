package ru.sportmaster.scd.algorithms.commmatrixm;

import static ru.sportmaster.scd.consts.ParamNames.COMMODITY_MATRIX_M_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.PARAMS_DEFINITIONS_WITH_CALC_DATE;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * Формирование многоинтервальной товарной матрицы SCD (Спортмастер + Остин). Этап 1. Шаг 2.
 */
@Component
public class CommodityMatrixMStage1Step2 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "COMMODITY_MATRIX_M_STAGE1_STEP2";
    private static final String EXECUTOR_DESCRIPTION =
        "Добавление пользовательского квотирования, учет пользовательских корректировок на уровне РЦМ / магазин"
            + " (Этап 1. Шаг 2.)";

    public CommodityMatrixMStage1Step2() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(COMMODITY_MATRIX_M_PACKAGE_NAME)
                .procedureName("stage_1_step_2")
                .paramDefinitions(PARAMS_DEFINITIONS_WITH_CALC_DATE)
                .build()
        );
    }
}
