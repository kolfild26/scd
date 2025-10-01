package ru.sportmaster.scd.algorithms.volumepreseason;

import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamNames.VOLUME_PRESEASON_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.VOLUME_INSEASON_PARAMS_DEFINITIONS;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * Расчет PreSeason объема товара на коллекцию (СМ, розница). Этап 1. Шаг 2.
 */
@Component
public class VolumePreseasonStage1Step2 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "VOLUME_PRESEASON_STAGE1_STEP2";
    private static final String EXECUTOR_DESCRIPTION =
            "Расчет PreSeason объема товара по данным заказа по PO (Этап 1. Шаг 2.)";

    public VolumePreseasonStage1Step2() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(VOLUME_PRESEASON_PACKAGE_NAME)
                .procedureName("stage_1_step_2")
                .paramDefinitions(VOLUME_INSEASON_PARAMS_DEFINITIONS)
                .build()
        );
    }
}
