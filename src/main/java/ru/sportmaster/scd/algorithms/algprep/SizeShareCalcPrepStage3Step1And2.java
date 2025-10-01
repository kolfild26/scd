package ru.sportmaster.scd.algorithms.algprep;

import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamNames.SIZE_SHARE_CALC_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.PARAMS_DEFINITIONS_PREP;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;

/**
 * Расчет стандартных размерных горок.
 * Подготовка исходных данных для расчета && Расчет стандартных размерных горок. Этап 3. Шаги 1 и 2.
 */
@Component
public class SizeShareCalcPrepStage3Step1And2 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "SIZE_SHARE_CALC_STAGE1_STEP1_2";
    private static final String EXECUTOR_DESCRIPTION =
        "Подготовка исходных данных для расчета / Расчет стандартных размерных горок. (Этап 3. Шаги 1 и 2.)";

    public SizeShareCalcPrepStage3Step1And2() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(SIZE_SHARE_CALC_PACKAGE_NAME)
                .procedureName("stage_1_step_1_2")
                .paramDefinitions(PARAMS_DEFINITIONS_PREP)
                .build()
        );
    }
}
