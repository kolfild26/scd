package ru.sportmaster.scd.algorithms.linkinglayers;

import static ru.sportmaster.scd.consts.ParamNames.LINKING_LAYERS_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.PARAMS_DEFINITIONS_PREP;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * Связка разработческого и мерчендайзингового слоев и группы товаров. Этап 2. Шаг 1.
 */
@Component
public class LinkingLayersStage2Step1 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "LINKING_LAYERS_STAGE2_STEP1";
    private static final String EXECUTOR_DESCRIPTION =
        "Отбор потокового товара (Этап 2. Шаг 1.)";

    public LinkingLayersStage2Step1() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(LINKING_LAYERS_PACKAGE_NAME)
                .procedureName("stage_2_step_1")
                .paramDefinitions(PARAMS_DEFINITIONS_PREP)
                .build()
        );
    }
}
