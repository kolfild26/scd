package ru.sportmaster.scd.consts;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * Константные значения имен параметров.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ParamNames {
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String REQUEST_UUID_FIELD = "requestUuid";
    public static final String USER_ID_FIELD = "userId";
    public static final String OS_USER_FIELD = "osUser";
    public static final String HOST_FIELD = "host";
    public static final String IP_ADDRESS_FIELD = "ipAddress";
    public static final String MODULE_FIELD = "module";
    public static final String USER_FIELD = "user";
    public static final String ENDPOINT_FIELD = "endpoint";

    public static final String SCD_SCHEMA_NAME = "scd";
    public static final String SCD_API_SCHEMA_NAME = "scd_api";

    public static final String LOCK_PACKAGE_NAME = "scd_p_lock";
    public static final String FRONT_API_PACKAGE_NAME = "ascd_p_front_api";
    public static final String PRESENTATION_CALC_PACKAGE_NAME = "scd_p_alg_prsnt_calc";
    public static final String PRESENTATION_CALC_M_PACKAGE_NAME = "scd_p_alg_prsnt_calc_m";
    public static final String COMMODITY_MATRIX_PACKAGE_NAME = "scd_p_alg_commodity_matrix";
    public static final String COMMODITY_MATRIX_M_PACKAGE_NAME = "scd_p_alg_commodity_matrix_m";
    public static final String REPLENISHMENT_STRATEGY_PACKAGE_NAME = "scd_p_alg_repl_strategy";
    public static final String REPLENISHMENT_STRATEGY_INSEASON_PACKAGE_NAME = "scd_p_alg_repl_strategy_inseason";
    public static final String FORECAST_MATRIX_PACKAGE_NAME = "scd_p_alg_forecast";
    public static final String FORECAST_PREP_PACKAGE_NAME = "scd_p_alg_forecast_prep";
    public static final String ALLOCATION_PACKAGE_NAME = "scd_p_alg_allocation";
    public static final String SIMPLE_3D_PLAN_PACKAGE_NAME = "scd_p_alg_3d_simple";
    public static final String PLAN_LIMITS_PACKAGE_NAME = "scd_p_alg_plan_limits";
    public static final String PLAN_LIMITS_INSEASON_PACKAGE_NAME = "scd_p_alg_plan_limits_inseason";
    public static final String LINKING_LAYERS_PACKAGE_NAME = "scd_p_alg_linking_layers";
    public static final String BACKEND_API_PACKAGE_NAME = "scd_p_backend_api";
    public static final String ROLLUP_PACKAGE_NAME = "scd_p_alg_rollup";
    public static final String VOLUME_INSEASON_PACKAGE_NAME = "scd_p_alg_volume_inseason";
    public static final String VOLUME_BASE_PACKAGE_NAME = "scd_p_alg_volume_base";
    public static final String VOLUME_PRESEASON_PACKAGE_NAME = "scd_p_alg_volume_preseason";
    public static final String SIZE_SHARE_CALC_PACKAGE_NAME = "scd_p_alg_size_share_calc";

    public static final String TRY_LOCK_PROCEDURE_NAME = "lock_step_algorithm";
    public static final String TRY_UNLOCK_PROCEDURE_NAME = "unlock_step_algorithm";
    public static final String GET_PIVOT_CONFIG_PROCEDURE_NAME = "get_config";
    public static final String SAVE_PIVOT_CONFIG_PROCEDURE_NAME = "save_config";
    public static final String GET_PIVOT_FILTERS_PROCEDURE_NAME = "get_filter_sets";
    public static final String SAVE_FILTER_SET_PROCEDURE_NAME = "save_filter_set";
    public static final String DELETE_FILTER_SETS_PROCEDURE_NAME = "delete_filter_sets";
    public static final String GET_PIVOT_DATA_PROCEDURE_NAME = "get_pivot_data";
    public static final String SEARCH_PIVOT_ROWS_PROCEDURE_NAME = "search_pivot_rows";
    public static final String DOWNLOAD_PIVOT_DATA_PROCEDURE_NAME = "download_pivot_data";
    public static final String GET_SAVE_PROGRESS_PROCEDURE_NAME = "get_save_progress";
    public static final String GET_ALGORITHM_DURATION_PROCEDURE_NAME = "get_alg_duration";
    public static final String GET_PIVOT_UNIQ_FIELD_VALUES_PROCEDURE_NAME = "get_pivot_uniq_field_values";
    public static final String PRICE_LEVEL_COPY_PROCEDURE_NAME = "price_level_copy";

    public static final String P_ID_PARTITION = "p_id_partition";
    public static final String P_PARTITION_TYPE = "p_partition_type";
    public static final String EXEC_ACTION_PARAM_NAME = "execAction";
    public static final String CURRENT_STAGE_PARAM_NAME = "currentStage";
    public static final String NEXT_STAGE_PARAM_NAME = "nextStage";
    public static final String CURRENT_STEP_PARAM_NAME = "currentStep";
    public static final String NEXT_STEP_PARAM_NAME = "nextStep";
    public static final String ERROR_STEP_PARAM_NAME = "errorStep";

    public static final String PARTITION_TYPE = "partition_type";
    public static final String ALGORITHM_ID = "algorithm_id";
    public static final String ALGORITHM_GROUP = "algorithm_group";
    public static final String CALC_ID = "calc_id";
    public static final String STOP_ON_ERROR = "stop_on_error";
    public static final String P_ID_STEP = "p_id_step";
    public static final String P_IS_FIX_STEP_RESULT = "p_is_fix_step_result";
    public static final String P_IS_SKIP_OUTLIER = "p_is_skip_outlier";
    public static final String P_LOG_GROUP = "p_log_group";
    public static final String P_SKIP_ERROR = "p_skip_error";
    public static final String P_ID_PARTITION_DIV_TMA = "p_id_partition_div_tma";
    public static final String P_CALC_DATE = "p_calc_date";
    public static final String P_COLLECTIONS_BEFORE = "p_collections_before";
    public static final String P_COLLECTION_BEFORE = "p_collection_before";
    public static final String P_COLLECTION = "p_collection";
    public static final String IS_DELETED = "isDeleted";
    public static final String P_PIVOT_NAME = "p_pivot_name";
    public static final String P_USER_ID = "p_user_id";
    public static final String P_START_ROW = "p_start_row";
    public static final String P_END_ROW = "p_end_row";
    public static final String P_TEXT = "p_text";
    public static final String P_PIVOT_ROWS = "p_pivot_rows";
    public static final String P_ALG_DEF_ID = "p_alg_def_id";
    public static final String P_DURATION_MS = "p_duration_ms";
    public static final String P_COLLAPSED_ORDER_ROWS = "p_collapsed_order_rows";
    public static final String P_FIELD = "p_field";
    public static final String P_DAG_ID = "p_dag_id";
    public static final String P_COUNTRY_GROUP = "p_country_group";

    public static final String P_PIVOT_CONFIG_PARAMS = "p_pivot_config_params";
    public static final String P_PIVOT_CONFIG = "p_pivot_config";
    public static final String P_PIVOT_FILTERS = "p_pivot_filters";
    public static final String P_PIVOT_FILTER = "p_pivot_filter";
    public static final String P_NAMES = "p_names";
    public static final String P_PIVOT_DATA = "p_pivot_data";
    public static final String P_PROGRESS = "p_progress";
    public static final String P_VALUES = "p_values";

    public static final String STRING_ARRAY_TYPE_NAME = "TP_STRINGS";

    public static final String DOCUMENT = "document";

    public static final String DC_PACKAGE_NAME = "scd_dc_correction";
    public static final String DC_API_PACKAGE_NAME = "scd_dc_api_scd";
    public static final String P_ID_CORRECTION_LIST = "p_id_correction_list";
    public static final String P_ID_CORRECTION_TYPE = "p_id_correction_type";
    public static final String P_DESCRIPTION = "p_description";
    public static final String P_ID_BUSINESS_TMA = "p_id_business_tma";
    public static final String P_CORRECTION_DET_LIST = "p_correction_det_list";
    public static final String P_CORRECTION_DET_LIST_MERGE = "p_correction_det_list_merge";
    public static final String P_CORRECTION_DET_LIST_DEL = "p_correction_det_list_del";
    public static final String P_ERROR_CODE = "p_error_code";
    public static final String P_ERROR_TEXT = "p_error_text";
    public static final String P_ID_REF_USER = "p_id_ref_user";
    public static final String CREATE_CORRECTION_PROCEDURE_NAME = "create_correction";
    public static final String UPDATE_CORRECTION_PROCEDURE_NAME = "update_correction";
    public static final String REMOVE_CORRECTION_PROCEDURE_NAME = "remove_correction";
    public static final String APPROVE_CORRECTION_PROCEDURE_NAME = "approve_correction";
    public static final String CANCEL_CORRECTION_PROCEDURE_NAME = "cancel_correction";

    public static final String SESSION_UUID_HEADER = "x-session-uuid";
    public static final String FORM_UUID_HEADER = "x-form-uuid";

    public static final String LOCATION_DEPARTMENT_ENTITY_NAME = "LocationDepartmentME";
    public static final String DEV_WARE_ENTITY_NAME = "DevWareME";
    public static final String MERCH_WARE_ENTITY_NAME = "MerchWareME";
    public static final String PREFIX_MEM_ENTITY_TOPIC_NAME = "MemoryEntityTopic";

    public static final String FIELD_SEPARATOR = "|";
    public static final String SELECT_ALL_STATEMENT = "select * from ";
    public static final String VALUE = "value";
    public static final String WEEK_VALUE_PREFIX = "week_";
    public static final String PIVOT_LEVEL_COLUMN = "sys_level_";
    public static final String PIVOT_HIERARCHY_COLUMN = "sys_name_";

    // Airflow
    public static final String ERROR_TYPE_RESPONSE_NAME = "type";
    public static final String ERROR_AUTH_RESPONSE_NAME = "detail";
    public static final String DAG_ID_RESPONSE_NAME = "dag_id";
    public static final String DAG_PAUSED_NAME = "is_paused";
    public static final String DAG_RUN_ID_RESPONSE_NAME = "dag_run_id";
    public static final String AIRFLOW_AUTH_TOKEN = "token";
    public static final String AIRFLOW_JWT_AUTH_TOKEN = "access_token";
}
