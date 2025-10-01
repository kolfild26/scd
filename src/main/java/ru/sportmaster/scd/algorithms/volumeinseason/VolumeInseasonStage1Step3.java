package ru.sportmaster.scd.algorithms.volumeinseason;

import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamNames.VOLUME_INSEASON_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.PARAMS_DEFINITIONS_PREP_WITH_CALC_DATE;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * Расчет InSeason объема товара на коллекцию (СМ, розница). Этап 1. Шаг 3.
 */
@Component
public class VolumeInseasonStage1Step3 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "VOLUME_INSEASON_STAGE1_STEP3";
    private static final String EXECUTOR_DESCRIPTION =
            "Расчет объема по МЦР / коллекция на основе прогноза остатков (Этап 1. Шаг 3.)";

    public VolumeInseasonStage1Step3() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(VOLUME_INSEASON_PACKAGE_NAME)
                .procedureName("stage_1_step_3")
                .paramDefinitions(PARAMS_DEFINITIONS_PREP_WITH_CALC_DATE)
                .build()
        );
    }
}
