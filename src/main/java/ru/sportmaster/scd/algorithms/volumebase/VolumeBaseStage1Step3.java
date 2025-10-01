package ru.sportmaster.scd.algorithms.volumebase;

import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamNames.VOLUME_BASE_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.PARAMS_DEFINITIONS_PREP_WITH_CALC_DATE;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * Расчет общей матрицы объема товара по прогнозу остатков. Этап 1. Шаг 3.
 */
@Component
public class VolumeBaseStage1Step3 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "VOLUME_BASE_STAGE1_STEP3";
    private static final String EXECUTOR_DESCRIPTION =
            "Расчет объема по МЦР / коллекция на основе прогноза остатков (Этап 1. Шаг 3.)";

    public VolumeBaseStage1Step3() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(VOLUME_BASE_PACKAGE_NAME)
                .procedureName("stage_1_step_3")
                .paramDefinitions(PARAMS_DEFINITIONS_PREP_WITH_CALC_DATE)
                .build()
        );
    }
}
