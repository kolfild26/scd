package ru.sportmaster.scd.algorithms.volumebase;

import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamNames.VOLUME_BASE_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.VOLUME_INSEASON_PARAMS_DEFINITIONS;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * Расчет общей матрицы объема товара по прогнозу остатков. Этап 1. Шаг 1.
 */
@Component
public class VolumeBaseStage1Step1 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "VOLUME_BASE_STAGE1_STEP1";
    private static final String EXECUTOR_DESCRIPTION =
            "Определение принадлежности МЦР на заданном периоде к коллекции (Этап 1. Шаг 1.)";

    public VolumeBaseStage1Step1() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(VOLUME_BASE_PACKAGE_NAME)
                .procedureName("stage_1_step_1")
                .paramDefinitions(VOLUME_INSEASON_PARAMS_DEFINITIONS)
                .build()
        );
    }
}
