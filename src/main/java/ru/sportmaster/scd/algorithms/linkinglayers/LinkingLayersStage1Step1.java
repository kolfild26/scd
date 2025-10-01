package ru.sportmaster.scd.algorithms.linkinglayers;

import static ru.sportmaster.scd.consts.ParamNames.LINKING_LAYERS_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.PARAMS_DEFINITIONS_PREP;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * Связка разработческого и мерчендайзингового слоев и группы товаров. Этап 1. Шаг 1.
 */
@Component
public class LinkingLayersStage1Step1 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "LINKING_LAYERS_STAGE1_STEP1";
    private static final String EXECUTOR_DESCRIPTION =
        "Предварительная связь между разработческими и мерчендайзинговыми цветомоделями (Этап 1. Шаг 1.)";

    public LinkingLayersStage1Step1() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(LINKING_LAYERS_PACKAGE_NAME)
                .procedureName("stage_1_step_1")
                .paramDefinitions(PARAMS_DEFINITIONS_PREP)
                .build()
        );
    }
}
