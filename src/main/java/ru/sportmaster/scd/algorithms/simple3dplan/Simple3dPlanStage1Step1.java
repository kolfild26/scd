package ru.sportmaster.scd.algorithms.simple3dplan;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static ru.sportmaster.scd.consts.ParamNames.EXEC_ACTION_PARAM_NAME;
import static ru.sportmaster.scd.consts.ParamNames.NEXT_STAGE_PARAM_NAME;
import static ru.sportmaster.scd.consts.ParamNames.NEXT_STEP_PARAM_NAME;
import static ru.sportmaster.scd.consts.ParamNames.P_COLLECTION;
import static ru.sportmaster.scd.consts.ParamNames.P_COLLECTION_BEFORE;
import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamNames.SIMPLE_3D_PLAN_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamsDefinitionsPredefined.SIMPLE_3D_PLAN_START_PARAMS_DEFINITIONS;

import java.util.Map;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.algorithms.AbstractAlgorithmJdbcCallStep;
import ru.sportmaster.scd.algorithms.StoredProcQueryDefinition;
import ru.sportmaster.scd.algorithms.executing.AlgorithmExecutingAction;

/**
 * Расчет 3D-Simple - плана продаж для ЦП (Спортмастер) (3D plan simple SCD). Этап 1. Шаг 1.
 */
@Component
public class Simple3dPlanStage1Step1 extends AbstractAlgorithmJdbcCallStep {
    private static final String EXECUTOR_NAME = "3D_PLAN_SIMPLE_STAGE1_STEP1";
    private static final String EXECUTOR_DESCRIPTION = "Определение расчетной коллекции (Этап 1. Шаг 1.)";
    private static final Integer STAGE_NEXT_POINT = 2;
    private static final Integer STEP_NEXT_POINT = 1;

    public Simple3dPlanStage1Step1() {
        super(
            EXECUTOR_NAME,
            EXECUTOR_DESCRIPTION,
            StoredProcQueryDefinition.builder()
                .schemaName(SCD_SCHEMA_NAME)
                .packageName(SIMPLE_3D_PLAN_PACKAGE_NAME)
                .procedureName("stage_1_step_1")
                .paramDefinitions(SIMPLE_3D_PLAN_START_PARAMS_DEFINITIONS)
                .build()
        );
    }

    @Override
    protected void processParams(Map<String, Object> params) {
        super.processParams(params);
        var collection = params.get(P_COLLECTION);
        if (isNull(collection)) {
            params.put(EXEC_ACTION_PARAM_NAME, AlgorithmExecutingAction.FINISH);
        } else {
            if (nonNull(params.get(P_COLLECTION_BEFORE))) {
                params.put(EXEC_ACTION_PARAM_NAME, AlgorithmExecutingAction.EXEC_POINT);
                params.put(NEXT_STAGE_PARAM_NAME, STAGE_NEXT_POINT);
                params.put(NEXT_STEP_PARAM_NAME, STEP_NEXT_POINT);
            } else {
                params.put(EXEC_ACTION_PARAM_NAME, AlgorithmExecutingAction.EXEC_NEXT);
            }
            params.put(P_COLLECTION_BEFORE, collection);
        }
    }
}
