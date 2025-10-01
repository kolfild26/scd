package ru.sportmaster.scd.algorithms.volumeinseason;

import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamNames.VOLUME_INSEASON_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.VOLUME_INSEASON_PARAMS_DEFINITIONS;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * Расчет InSeason объема товара на коллекцию (СМ, розница). Этап 1. Шаг 1.
 */
@Component
public class VolumeInseasonStage1Step1 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "VOLUME_INSEASON_STAGE1_STEP1";
    private static final String EXECUTOR_DESCRIPTION =
            "Формирование таблицы характерных периодов поставок (Этап 1. Шаг 1.)";

    public VolumeInseasonStage1Step1() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(VOLUME_INSEASON_PACKAGE_NAME)
                .procedureName("stage_1_step_1")
                .paramDefinitions(VOLUME_INSEASON_PARAMS_DEFINITIONS)
                .build()
        );
    }
}
