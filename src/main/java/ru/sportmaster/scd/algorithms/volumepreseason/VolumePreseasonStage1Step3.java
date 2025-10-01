package ru.sportmaster.scd.algorithms.volumepreseason;

import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamNames.VOLUME_PRESEASON_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.VOLUME_INSEASON_PARAMS_DEFINITIONS;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * Расчет PreSeason объема товара на коллекцию (СМ, розница). Этап 1. Шаг 3.
 */
@Component
public class VolumePreseasonStage1Step3 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "VOLUME_PRESEASON_STAGE1_STEP3";
    private static final String EXECUTOR_DESCRIPTION =
            "Пользовательские корректировки объема товара (Этап 1. Шаг 3.)";

    public VolumePreseasonStage1Step3() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(VOLUME_PRESEASON_PACKAGE_NAME)
                .procedureName("stage_1_step_3")
                .paramDefinitions(VOLUME_INSEASON_PARAMS_DEFINITIONS)
                .build()
        );
    }
}
