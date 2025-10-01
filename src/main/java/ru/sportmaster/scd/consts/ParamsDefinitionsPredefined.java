package ru.sportmaster.scd.consts;

import static ru.sportmaster.scd.consts.ParamNames.P_CALC_DATE;
import static ru.sportmaster.scd.consts.ParamNames.P_COLLECTION;
import static ru.sportmaster.scd.consts.ParamNames.P_COLLECTIONS_BEFORE;
import static ru.sportmaster.scd.consts.ParamNames.P_COLLECTION_BEFORE;
import static ru.sportmaster.scd.consts.ParamNames.P_ID_BUSINESS_TMA;
import static ru.sportmaster.scd.consts.ParamNames.P_ID_PARTITION_DIV_TMA;
import static ru.sportmaster.scd.consts.ParamNames.P_ID_STEP;
import static ru.sportmaster.scd.consts.ParamNames.P_IS_FIX_STEP_RESULT;
import static ru.sportmaster.scd.consts.ParamNames.P_IS_SKIP_OUTLIER;
import static ru.sportmaster.scd.consts.ParamNames.P_LOG_GROUP;
import static ru.sportmaster.scd.consts.ParamNames.REQUEST_UUID_FIELD;

import jakarta.persistence.ParameterMode;
import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import ru.sportmaster.scd.algorithms.ParamDefinition;
import ru.sportmaster.scd.utils.ConvertUtil;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ParamsDefinitionsPredefined {
    public static final List<ParamDefinition> FULL_PARAMS_DEFINITIONS =
        List.of(
            new ParamDefinition(P_ID_STEP, Long.class, ConvertUtil::getLongValue, true),
            new ParamDefinition(P_ID_PARTITION_DIV_TMA, Long.class, ConvertUtil::getLongValue),
            new ParamDefinition(P_CALC_DATE, LocalDate.class, ConvertUtil::getDateValue),
            new ParamDefinition(P_COLLECTIONS_BEFORE, Long.class, ConvertUtil::getLongValue),
            new ParamDefinition(P_IS_FIX_STEP_RESULT, Long.class, ConvertUtil::getLongValue),
            new ParamDefinition(P_LOG_GROUP, String.class, REQUEST_UUID_FIELD)
        );

    public static final List<ParamDefinition> PARAMS_DEFINITIONS =
        List.of(
            new ParamDefinition(P_ID_STEP, Long.class, ConvertUtil::getLongValue, true),
            new ParamDefinition(P_ID_PARTITION_DIV_TMA, Long.class, ConvertUtil::getLongValue),
            new ParamDefinition(P_IS_FIX_STEP_RESULT, Long.class, ConvertUtil::getLongValue),
            new ParamDefinition(P_LOG_GROUP, String.class, REQUEST_UUID_FIELD)
        );

    public static final List<ParamDefinition> PARAMS_DEFINITIONS_WITH_CALC_DATE =
        List.of(
            new ParamDefinition(P_ID_STEP, Long.class, ConvertUtil::getLongValue, true),
            new ParamDefinition(P_ID_PARTITION_DIV_TMA, Long.class, ConvertUtil::getLongValue),
            new ParamDefinition(P_CALC_DATE, LocalDate.class, ConvertUtil::getDateValue),
            new ParamDefinition(P_IS_FIX_STEP_RESULT, Long.class, ConvertUtil::getLongValue),
            new ParamDefinition(P_LOG_GROUP, String.class, REQUEST_UUID_FIELD)
        );

    public static final List<ParamDefinition> PARAMS_DEFINITIONS_PREP_WITH_CALC_DATE =
        List.of(
            new ParamDefinition(P_ID_STEP, Long.class, ConvertUtil::getLongValue, true),
            new ParamDefinition(P_CALC_DATE, LocalDate.class, ConvertUtil::getDateValue),
            new ParamDefinition(P_IS_FIX_STEP_RESULT, Long.class, ConvertUtil::getLongValue),
            new ParamDefinition(P_LOG_GROUP, String.class, REQUEST_UUID_FIELD)
        );

    public static final List<ParamDefinition> PARAMS_DEFINITIONS_PREP =
        List.of(
            new ParamDefinition(P_ID_STEP, Long.class, ConvertUtil::getLongValue, true),
            new ParamDefinition(P_IS_FIX_STEP_RESULT, Long.class, ConvertUtil::getLongValue),
            new ParamDefinition(P_LOG_GROUP, String.class, REQUEST_UUID_FIELD)
        );

    public static final List<ParamDefinition> ALLOCATION_PARAMS_DEFINITIONS =
        List.of(
            new ParamDefinition(P_ID_STEP, Long.class, ConvertUtil::getLongValue, true),
            new ParamDefinition(P_ID_PARTITION_DIV_TMA, Long.class, ConvertUtil::getLongValue),
            new ParamDefinition(P_IS_FIX_STEP_RESULT, Long.class, ConvertUtil::getLongValue),
            new ParamDefinition(P_CALC_DATE, LocalDate.class, ConvertUtil::getDateValue),
            new ParamDefinition(P_LOG_GROUP, String.class, REQUEST_UUID_FIELD)
        );

    public static final List<ParamDefinition> ALLOCATION_PREP_PARAMS_DEFINITIONS =
        List.of(
            new ParamDefinition(P_ID_STEP, Long.class, ConvertUtil::getLongValue, true),
            new ParamDefinition(P_IS_FIX_STEP_RESULT, Long.class, ConvertUtil::getLongValue),
            new ParamDefinition(P_CALC_DATE, LocalDate.class, ConvertUtil::getDateValue),
            new ParamDefinition(P_LOG_GROUP, String.class, REQUEST_UUID_FIELD)
        );

    public static final List<ParamDefinition> SIMPLE_3D_PLAN_START_PARAMS_DEFINITIONS =
        List.of(
            new ParamDefinition(P_ID_STEP, Long.class, ConvertUtil::getLongValue, true),
            new ParamDefinition(P_ID_BUSINESS_TMA, Long.class, ConvertUtil::getLongValue),
            new ParamDefinition(P_CALC_DATE, LocalDate.class, ConvertUtil::getDateValue),
            new ParamDefinition(P_COLLECTION_BEFORE, Long.class, ConvertUtil::getLongValue),
            new ParamDefinition(P_COLLECTION, Long.class, ParameterMode.OUT, ConvertUtil::getLongValue),
            new ParamDefinition(P_IS_FIX_STEP_RESULT, Long.class, ConvertUtil::getLongValue),
            new ParamDefinition(P_LOG_GROUP, String.class, REQUEST_UUID_FIELD)
        );

    public static final List<ParamDefinition> SIMPLE_3D_PLAN_PARAMS_DEFINITIONS =
        List.of(
            new ParamDefinition(P_ID_STEP, Long.class, ConvertUtil::getLongValue, true),
            new ParamDefinition(P_ID_BUSINESS_TMA, Long.class, ConvertUtil::getLongValue),
            new ParamDefinition(P_COLLECTION, Long.class, ConvertUtil::getLongValue),
            new ParamDefinition(P_IS_FIX_STEP_RESULT, Long.class, ConvertUtil::getLongValue),
            new ParamDefinition(P_LOG_GROUP, String.class, REQUEST_UUID_FIELD)
        );

    public static final List<ParamDefinition> SIMPLE_3D_PLAN_SIMPLE_PARAMS_DEFINITIONS =
        List.of(
            new ParamDefinition(P_ID_STEP, Long.class, ConvertUtil::getLongValue, true),
            new ParamDefinition(P_ID_BUSINESS_TMA, Long.class, ConvertUtil::getLongValue),
            new ParamDefinition(P_IS_FIX_STEP_RESULT, Long.class, ConvertUtil::getLongValue),
            new ParamDefinition(P_LOG_GROUP, String.class, REQUEST_UUID_FIELD)
        );

    public static final List<ParamDefinition> SIMPLE_3D_PLAN_SKIP_OUTLIER_PARAMS_DEFINITIONS =
        List.of(
            new ParamDefinition(P_ID_STEP, Long.class, ConvertUtil::getLongValue, true),
            new ParamDefinition(P_ID_BUSINESS_TMA, Long.class, ConvertUtil::getLongValue),
            new ParamDefinition(P_COLLECTION, Long.class, ConvertUtil::getLongValue),
            new ParamDefinition(P_IS_SKIP_OUTLIER, Long.class, ConvertUtil::getLongValue),
            new ParamDefinition(P_IS_FIX_STEP_RESULT, Long.class, ConvertUtil::getLongValue),
            new ParamDefinition(P_LOG_GROUP, String.class, REQUEST_UUID_FIELD)
        );

    public static final List<ParamDefinition> SIMPLE_3D_PLAN_EXTEND_PARAMS_DEFINITIONS =
        List.of(
            new ParamDefinition(P_ID_STEP, Long.class, ConvertUtil::getLongValue, true),
            new ParamDefinition(P_ID_BUSINESS_TMA, Long.class, ConvertUtil::getLongValue),
            new ParamDefinition(P_COLLECTION, Long.class, ConvertUtil::getLongValue),
            new ParamDefinition(P_CALC_DATE, LocalDate.class, ConvertUtil::getDateValue),
            new ParamDefinition(P_IS_FIX_STEP_RESULT, Long.class, ConvertUtil::getLongValue),
            new ParamDefinition(P_LOG_GROUP, String.class, REQUEST_UUID_FIELD)
        );

    public static final List<ParamDefinition> SIMPLE_3D_PLAN_CALC_DATE_PARAMS_DEFINITIONS =
        List.of(
            new ParamDefinition(P_ID_STEP, Long.class, ConvertUtil::getLongValue, true),
            new ParamDefinition(P_ID_BUSINESS_TMA, Long.class, ConvertUtil::getLongValue),
            new ParamDefinition(P_CALC_DATE, LocalDate.class, ConvertUtil::getDateValue),
            new ParamDefinition(P_IS_FIX_STEP_RESULT, Long.class, ConvertUtil::getLongValue),
            new ParamDefinition(P_LOG_GROUP, String.class, REQUEST_UUID_FIELD)
        );

    public static final List<ParamDefinition> SIMPLE_3D_PLAN_PREPARE_PARAMS_DEFINITIONS =
        List.of(
            new ParamDefinition(P_ID_STEP, Long.class, ConvertUtil::getLongValue, true),
            new ParamDefinition(P_LOG_GROUP, String.class, REQUEST_UUID_FIELD)
        );

    public static final List<ParamDefinition> VOLUME_INSEASON_PARAMS_DEFINITIONS =
        List.of(
            new ParamDefinition(P_ID_STEP, Long.class, ConvertUtil::getLongValue, true),
            new ParamDefinition(P_CALC_DATE, LocalDate.class, ConvertUtil::getDateValue),
            new ParamDefinition(P_COLLECTIONS_BEFORE, Long.class, ConvertUtil::getLongValue),
            new ParamDefinition(P_IS_FIX_STEP_RESULT, Long.class, ConvertUtil::getLongValue),
            new ParamDefinition(P_LOG_GROUP, String.class, REQUEST_UUID_FIELD)
        );
}
