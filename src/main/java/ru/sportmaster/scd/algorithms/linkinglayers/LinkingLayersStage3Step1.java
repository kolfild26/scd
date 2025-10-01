package ru.sportmaster.scd.algorithms.linkinglayers;

import static ru.sportmaster.scd.consts.ParamNames.LINKING_LAYERS_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.PARAMS_DEFINITIONS_PREP;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * Связка разработческого и мерчендайзингового слоев и группы товаров. Этап 3. Шаг 1.
 */
@Component
public class LinkingLayersStage3Step1 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "LINKING_LAYERS_STAGE3_STEP1";
    private static final String EXECUTOR_DESCRIPTION =
        "Отбор остального товара (Этап 3. Шаг 1.)";

    public LinkingLayersStage3Step1() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(LINKING_LAYERS_PACKAGE_NAME)
                .procedureName("stage_3_step_1")
                .paramDefinitions(PARAMS_DEFINITIONS_PREP)
                .build()
        );
    }
}
