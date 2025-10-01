package ru.sportmaster.scd.algorithms.volumeinseason;

import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamNames.VOLUME_INSEASON_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.VOLUME_INSEASON_PARAMS_DEFINITIONS;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * Расчет InSeason объема товара на коллекцию (СМ, розница). Этап 1. Шаг 2.
 */
@Component
public class VolumeInseasonStage1Step2 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "VOLUME_INSEASON_STAGE1_STEP2";
    private static final String EXECUTOR_DESCRIPTION =
            "Определение коллекционной связки МЦР - РЦР (Этап 1. Шаг 2.)";

    public VolumeInseasonStage1Step2() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(VOLUME_INSEASON_PACKAGE_NAME)
                .procedureName("stage_1_step_2")
                .paramDefinitions(VOLUME_INSEASON_PARAMS_DEFINITIONS)
                .build()
        );
    }
}
