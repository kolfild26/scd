package ru.sportmaster.scd.algorithms.volumepreseason;

import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamNames.VOLUME_PRESEASON_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.PARAMS_DEFINITIONS_PREP_WITH_CALC_DATE;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * Расчет PreSeason объема товара на коллекцию (СМ, розница). Этап 1. Шаг 1.
 */
@Component
public class VolumePreseasonStage1Step1 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "VOLUME_PRESEASON_STAGE1_STEP1";
    private static final String EXECUTOR_DESCRIPTION =
            "Расчет PreSeason объема товара по данным прогноза остатков (Этап 1. Шаг 1.)";

    public VolumePreseasonStage1Step1() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(VOLUME_PRESEASON_PACKAGE_NAME)
                .procedureName("stage_1_step_1")
                .paramDefinitions(PARAMS_DEFINITIONS_PREP_WITH_CALC_DATE)
                .build()
        );
    }
}
