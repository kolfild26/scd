package ru.sportmaster.scd.entity.adjustment;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static ru.sportmaster.scd.utils.JsonUtil.getNullableValue;
import static ru.sportmaster.scd.utils.UiUtil.DATE_FORMAT;
import static ru.sportmaster.scd.utils.UiUtil.DB_CALENDAR_ID_FORMAT;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.POJONode;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.sportmaster.scd.entity.adjustment.struct.ITpAdjustmentRow;
import ru.sportmaster.scd.entity.adjustment.struct.TpCoefficientUpperPlanLimitDSC;
import ru.sportmaster.scd.entity.adjustment.struct.TpCoefficientUpperPlanLimitMSC;
import ru.sportmaster.scd.entity.adjustment.struct.TpCoefficientWSSAllocationDSC;
import ru.sportmaster.scd.entity.adjustment.struct.TpCoefficientWSSAllocationMSC;
import ru.sportmaster.scd.entity.adjustment.struct.TpDSCVal;
import ru.sportmaster.scd.entity.adjustment.struct.TpIntakeExitDateDSC;
import ru.sportmaster.scd.entity.adjustment.struct.TpIntakeExitDateMSC;
import ru.sportmaster.scd.entity.adjustment.struct.TpLcDatesDSC;
import ru.sportmaster.scd.entity.adjustment.struct.TpLcDatesMSC;
import ru.sportmaster.scd.entity.adjustment.struct.TpLcMinUpaDSC;
import ru.sportmaster.scd.entity.adjustment.struct.TpLcMinUpaDSCS;
import ru.sportmaster.scd.entity.adjustment.struct.TpLcMinUpaMSC;
import ru.sportmaster.scd.entity.adjustment.struct.TpLcMinUpaMSCS;
import ru.sportmaster.scd.entity.adjustment.struct.TpLcSlsrgnBsnssStReptypeDSCS;
import ru.sportmaster.scd.entity.adjustment.struct.TpManualDayWeight;
import ru.sportmaster.scd.entity.adjustment.struct.TpMinUpaDSC;
import ru.sportmaster.scd.entity.adjustment.struct.TpMinUpaDSCS;
import ru.sportmaster.scd.entity.adjustment.struct.TpMinUpaMSC;
import ru.sportmaster.scd.entity.adjustment.struct.TpMinUpaMSCS;
import ru.sportmaster.scd.entity.adjustment.struct.TpPlan3DMtpl;
import ru.sportmaster.scd.entity.adjustment.struct.TpPlan3DVal;
import ru.sportmaster.scd.entity.adjustment.struct.TpPlanLimitDSC;
import ru.sportmaster.scd.entity.adjustment.struct.TpPlanLimitMSC;
import ru.sportmaster.scd.entity.adjustment.struct.TpReplEndDateMSCS;
import ru.sportmaster.scd.entity.adjustment.struct.TpReplenishmentEndDateSalesChannelDSC;
import ru.sportmaster.scd.entity.adjustment.struct.TpReplenishmentEndDateSalesChannelMSC;
import ru.sportmaster.scd.entity.adjustment.struct.TpReplenishmentEndDateStoreDSC;
import ru.sportmaster.scd.entity.adjustment.struct.TpReplenishmentEndDateStoreMSC;
import ru.sportmaster.scd.entity.adjustment.struct.TpReplenishmentStrategyDSC;
import ru.sportmaster.scd.entity.adjustment.struct.TpReplenishmentStrategyMSC;
import ru.sportmaster.scd.entity.adjustment.struct.TpStartListStoreDSC;
import ru.sportmaster.scd.entity.adjustment.struct.TpStartListStoreDSCS;
import ru.sportmaster.scd.entity.adjustment.struct.TpStartListStoreMSC;
import ru.sportmaster.scd.entity.adjustment.struct.TpStartListStoreMSCS;
import ru.sportmaster.scd.entity.adjustment.struct.TpStopListBusinessCountryDSC;
import ru.sportmaster.scd.entity.adjustment.struct.TpStopListBusinessCountryDSCS;
import ru.sportmaster.scd.entity.adjustment.struct.TpStopListBusinessCountryMSC;
import ru.sportmaster.scd.entity.adjustment.struct.TpStopListBusinessCountryMSCS;
import ru.sportmaster.scd.entity.adjustment.struct.TpStopListStoreDSC;
import ru.sportmaster.scd.entity.adjustment.struct.TpStopListStoreDSCS;
import ru.sportmaster.scd.entity.adjustment.struct.TpStopListStoreMSC;
import ru.sportmaster.scd.entity.adjustment.struct.TpStopListStoreMSCS;
import ru.sportmaster.scd.entity.adjustment.struct.TpStoreOpenStoreCloseDate;
import ru.sportmaster.scd.entity.adjustment.struct.TpWeekStoreForecastMSC;
import ru.sportmaster.scd.entity.dictionary.Category_;
import ru.sportmaster.scd.entity.dictionary.Collection_;
import ru.sportmaster.scd.entity.dictionary.RDHierarchyGeneral_;
import ru.sportmaster.scd.entity.pivot.CountryGroup_;
import ru.sportmaster.scd.entity.pivot.Country_;
import ru.sportmaster.scd.entity.pivot.DivisionTMA_;
import ru.sportmaster.scd.entity.pivot.LocationDepartment_;
import ru.sportmaster.scd.entity.replenishment.ReplenishmentStrategy_;
import ru.sportmaster.scd.utils.ConvertUtil;

@Getter
@AllArgsConstructor
public enum AdjustmentType {
    STOP_LIST_STORE_MSCS(
        StopListStoreMSCS.class, 0,
        "coll_mscs_shop_stl",
            value -> TpStopListStoreMSCS.builder()
            .id(getNullableLongValue(value, StopListStoreMSCS_.ID))
            .collectionId(getNullableLongValue(value, StopListStoreMSCS_.COLLECTION, Collection_.ID))
            .sizeId(getNullableLongValue(value, StopListStoreMSCS_.SIZE, MerchColorSize_.ID))
            .departmentId(getNullableLongValue(value, StopListStoreMSCS_.SHOP, LocationDepartment_.ID))
            .stlType(getNullableBoolValue(value, StopListStoreMSCS_.STL_TYPE))
            .build()
    ),
    STOP_LIST_BUSINESS_COUNTRY_MSCS(
        StopListBusinessCountryMSCS.class, 1,
        "coll_mscs_country_dtma_stl",
            value -> TpStopListBusinessCountryMSCS.builder()
            .id(getNullableLongValue(value, StopListBusinessCountryMSCS_.ID))
            .collectionId(getNullableLongValue(value, StopListBusinessCountryMSCS_.COLLECTION, Collection_.ID))
            .sizeId(getNullableLongValue(value, StopListBusinessCountryMSCS_.SIZE, MerchColorSize_.ID))
            .countryId(getNullableLongValue(value, StopListBusinessCountryMSCS_.COUNTRY, Country_.ID))
            .divisionId(getNullableLongValue(value, StopListBusinessCountryMSCS_.DIVISION, DivisionTMA_.ID))
            .stlType(getNullableBoolValue(value, StopListBusinessCountryMSCS_.STL_TYPE))
            .build()
    ),
    STOP_LIST_STORE_MSC(
        StopListStoreMSC.class, 2,
        "coll_msc_shop_stl",
            value -> TpStopListStoreMSC.builder()
            .id(getNullableLongValue(value, StopListStoreMSC_.ID))
            .collectionId(getNullableLongValue(value, StopListStoreMSC_.COLLECTION, Collection_.ID))
            .modelId(getNullableLongValue(value, StopListStoreMSC_.MODEL, MerchColorModel_.ID))
            .departmentId(getNullableLongValue(value, StopListStoreMSC_.SHOP, LocationDepartment_.ID))
            .stlType(getNullableBoolValue(value, StopListStoreMSC_.STL_TYPE))
            .build()
    ),
    STOP_LIST_BUSINESS_COUNTRY_MSC(
        StopListBusinessCountryMSC.class, 3,
        "coll_msc_country_dtma_stl",
            value -> TpStopListBusinessCountryMSC.builder()
            .id(getNullableLongValue(value, StopListBusinessCountryMSC_.ID))
            .collectionId(getNullableLongValue(value, StopListBusinessCountryMSC_.COLLECTION, Collection_.ID))
            .modelId(getNullableLongValue(value, StopListBusinessCountryMSC_.MODEL, MerchColorModel_.ID))
            .countryId(getNullableLongValue(value, StopListBusinessCountryMSC_.COUNTRY, Country_.ID))
            .divisionId(getNullableLongValue(value, StopListBusinessCountryMSC_.DIVISION, DivisionTMA_.ID))
            .stlType(getNullableBoolValue(value, StopListBusinessCountryMSC_.STL_TYPE))
            .build()
    ),
    STOP_LIST_STORE_DSCS(
        StopListStoreDSCS.class, 4,
        "coll_dscs_shop_stl",
            value -> TpStopListStoreDSCS.builder()
            .id(getNullableLongValue(value, StopListStoreDSCS_.ID))
            .collectionId(getNullableLongValue(value, StopListStoreDSCS_.COLLECTION, Collection_.ID))
            .sizeId(getNullableLongValue(value, StopListStoreDSCS_.SIZE, DevColorSize_.ID))
            .departmentId(getNullableLongValue(value, StopListStoreDSCS_.SHOP, LocationDepartment_.ID))
            .stlType(getNullableBoolValue(value, StopListStoreDSCS_.STL_TYPE))
            .build()
    ),
    STOP_LIST_BUSINESS_COUNTRY_DSCS(
        StopListBusinessCountryDSCS.class, 5,
        "coll_dscs_country_dtma_stl",
            value -> TpStopListBusinessCountryDSCS.builder()
            .id(getNullableLongValue(value, StopListBusinessCountryDSCS_.ID))
            .collectionId(getNullableLongValue(value, StopListBusinessCountryDSCS_.COLLECTION, Collection_.ID))
            .sizeId(getNullableLongValue(value, StopListBusinessCountryDSCS_.SIZE, MerchColorSize_.ID))
            .countryId(getNullableLongValue(value, StopListBusinessCountryDSCS_.COUNTRY, Country_.ID))
            .divisionId(getNullableLongValue(value, StopListBusinessCountryDSCS_.DIVISION, DivisionTMA_.ID))
            .stlType(getNullableBoolValue(value, StopListBusinessCountryDSCS_.STL_TYPE))
            .build()
    ),
    STOP_LIST_STORE_DSC(
        StopListStoreDSC.class, 6,
        "coll_dsc_shop_stl",
            value -> TpStopListStoreDSC.builder()
            .id(getNullableLongValue(value, StopListStoreDSC_.ID))
            .collectionId(getNullableLongValue(value, StopListStoreDSC_.COLLECTION, Collection_.ID))
            .modelId(getNullableLongValue(value, StopListStoreDSC_.MODEL, MerchColorModel_.ID))
            .departmentId(getNullableLongValue(value, StopListStoreDSC_.SHOP, LocationDepartment_.ID))
            .stlType(getNullableBoolValue(value, StopListStoreDSC_.STL_TYPE))
            .build()
    ),
    STOP_LIST_BUSINESS_COUNTRY_DSC(
        StopListBusinessCountryDSC.class, 7,
        "coll_dsc_country_dtma_stl",
            value -> TpStopListBusinessCountryDSC.builder()
            .id(getNullableLongValue(value, StopListBusinessCountryDSC_.ID))
            .collectionId(getNullableLongValue(value, StopListBusinessCountryDSC_.COLLECTION, Collection_.ID))
            .modelId(getNullableLongValue(value, StopListBusinessCountryDSC_.MODEL, MerchColorModel_.ID))
            .countryId(getNullableLongValue(value, StopListBusinessCountryDSC_.COUNTRY, Country_.ID))
            .divisionId(getNullableLongValue(value, StopListBusinessCountryDSC_.DIVISION, DivisionTMA_.ID))
            .stlType(getNullableBoolValue(value, StopListBusinessCountryDSC_.STL_TYPE))
            .build()
    ),
    MIN_UPA_MSC(
        MinUpaMSC.class, 8,
        "coll_msc_shop_pt_exts_category_min_upa",
            value -> TpMinUpaMSC.builder()
            .id(getNullableLongValue(value, MinUpaMSC_.ID))
            .collectionId(getNullableLongValue(value, MinUpaMSC_.COLLECTION, Collection_.ID))
            .modelId(getNullableLongValue(value, MinUpaMSC_.MODEL, MerchColorModel_.ID))
            .departmentId(getNullableLongValue(value, MinUpaMSC_.SHOP, LocationDepartment_.ID))
            .categoryId(getNullableLongValue(value, MinUpaMSC_.CATEGORY, Category_.ID))
            .updType(getNullableValue(value, JsonNode::asText, MinUpaMSC_.PRESENTATION_TYPE))
            .minUpa(getNullableIntValue(value, MinUpaMSC_.UPA))
            .extSizeId(getNullableLongValue(value, MinUpaMSC_.EXT_SIZE, ScaleSize_.ID))
            .build()
    ),
    MIN_UPA_DSC(
        MinUpaDSC.class, 9,
        "coll_dsc_shop_pt_exts_category_min_upa",
            value -> TpMinUpaDSC.builder()
            .id(getNullableLongValue(value, MinUpaDSC_.ID))
            .collectionId(getNullableLongValue(value, MinUpaDSC_.COLLECTION, Collection_.ID))
            .modelId(getNullableLongValue(value, MinUpaDSC_.MODEL, DevColorModel_.ID))
            .departmentId(getNullableLongValue(value, MinUpaDSC_.SHOP, LocationDepartment_.ID))
            .categoryId(getNullableLongValue(value, MinUpaDSC_.CATEGORY, Category_.ID))
            .updType(getNullableValue(value, JsonNode::asText, MinUpaDSC_.PRESENTATION_TYPE))
            .minUpa(getNullableIntValue(value, MinUpaDSC_.UPA))
            .extSizeId(getNullableLongValue(value, MinUpaDSC_.EXT_SIZE, ScaleSize_.ID))
            .build()
    ),
    INTAKE_EXIT_DATE_MSC(
        IntakeExitDateMSC.class, 10,
        "coll_msc_shop_inex_dat",
            value -> TpIntakeExitDateMSC.builder()
            .id(getNullableLongValue(value, IntakeExitDateMSC_.ID))
            .collectionId(getNullableLongValue(value, IntakeExitDateMSC_.COLLECTION, Collection_.ID))
            .modelId(getNullableLongValue(value, IntakeExitDateMSC_.MODEL, MerchColorModel_.ID))
            .departmentId(getNullableLongValue(value, IntakeExitDateMSC_.SHOP, LocationDepartment_.ID))
            .intakeDate(getNullableDateValue(value, IntakeExitDateMSC_.INTAKE_DATE))
            .exitDate(getNullableDateValue(value, IntakeExitDateMSC_.EXIT_DATE))
            .build()
    ),
    INTAKE_EXIT_DATE_DSC(
        IntakeExitDateDSC.class, 11,
        "coll_dsc_shop_inex_dat",
            value -> TpIntakeExitDateDSC.builder()
            .id(getNullableLongValue(value, IntakeExitDateDSC_.ID))
            .collectionId(getNullableLongValue(value, IntakeExitDateDSC_.COLLECTION, Collection_.ID))
            .modelId(getNullableLongValue(value, IntakeExitDateDSC_.MODEL, DevColorModel_.ID))
            .departmentId(getNullableLongValue(value, IntakeExitDateDSC_.SHOP, LocationDepartment_.ID))
            .intakeDate(getNullableDateValue(value, IntakeExitDateDSC_.INTAKE_DATE))
            .exitDate(getNullableDateValue(value, IntakeExitDateDSC_.EXIT_DATE))
            .build()
    ),
    REPLENISHMENT_END_DATE_STORE_MSC(
        ReplenishmentEndDateStoreMSC.class, 12,
        "coll_msc_shop_repl_end_dat",
            value -> TpReplenishmentEndDateStoreMSC.builder()
            .id(getNullableLongValue(value, ReplenishmentEndDateStoreMSC_.ID))
            .collectionId(getNullableLongValue(value, ReplenishmentEndDateStoreMSC_.COLLECTION, Collection_.ID))
            .modelId(getNullableLongValue(value, ReplenishmentEndDateStoreMSC_.MODEL, MerchColorModel_.ID))
            .departmentId(getNullableLongValue(value, ReplenishmentEndDateStoreMSC_.SHOP, LocationDepartment_.ID))
            .replDate(getNullableDateValue(value, ReplenishmentEndDateStoreMSC_.VALUE))
            .build()
    ),
    REPLENISHMENT_END_DATE_SC_MSC(
        ReplenishmentEndDateSChannelMSC.class, 13,
        "coll_msc_dtma_repl_end_dat",
            value -> TpReplenishmentEndDateSalesChannelMSC.builder()
            .id(getNullableLongValue(value, ReplenishmentEndDateSChannelMSC_.ID))
            .collectionId(getNullableLongValue(value, ReplenishmentEndDateSChannelMSC_.COLLECTION, Collection_.ID))
            .modelId(getNullableLongValue(value, ReplenishmentEndDateSChannelMSC_.MODEL, MerchColorModel_.ID))
            .divisionId(getNullableLongValue(value, ReplenishmentEndDateSChannelMSC_.DIVISION, LocationDepartment_.ID))
            .replEndDate(getNullableDateValue(value, ReplenishmentEndDateSChannelMSC_.VALUE))
            .build()
    ),
    REPLENISHMENT_END_DATE_STORE_DSC(
        ReplenishmentEndDateStoreDSC.class, 14,
        "coll_dsc_shop_repl_end_dat",
            value -> TpReplenishmentEndDateStoreDSC.builder()
            .id(getNullableLongValue(value, ReplenishmentEndDateStoreDSC_.ID))
            .collectionId(getNullableLongValue(value, ReplenishmentEndDateStoreDSC_.COLLECTION, Collection_.ID))
            .modelId(getNullableLongValue(value, ReplenishmentEndDateStoreDSC_.MODEL, DevColorModel_.ID))
            .departmentId(getNullableLongValue(value, ReplenishmentEndDateStoreDSC_.SHOP, LocationDepartment_.ID))
            .replEndDate(getNullableDateValue(value, ReplenishmentEndDateStoreDSC_.VALUE))
            .build()
    ),
    REPLENISHMENT_END_DATE_SC_DSC(
        ReplenishmentEndDateSChannelDSC.class, 15,
        "coll_dsc_dtma_repl_end_dat",
            value -> TpReplenishmentEndDateSalesChannelDSC.builder()
            .id(getNullableLongValue(value, ReplenishmentEndDateSChannelMSC_.ID))
            .collectionId(getNullableLongValue(value, ReplenishmentEndDateSChannelMSC_.COLLECTION, Collection_.ID))
            .modelId(getNullableLongValue(value, ReplenishmentEndDateSChannelMSC_.MODEL, DevColorModel_.ID))
            .divisionId(getNullableLongValue(value, ReplenishmentEndDateSChannelMSC_.DIVISION, LocationDepartment_.ID))
            .replEndDate(getNullableDateValue(value, ReplenishmentEndDateSChannelMSC_.VALUE))
            .build()
    ),
    STORE_OPEN_STORE_CLOSE_DATE(
        StoreOpenStoreCloseDate.class, 16,
        "shop_opcl_dat",
            value -> TpStoreOpenStoreCloseDate.builder()
            .id(getNullableLongValue(value, StoreOpenStoreCloseDate_.ID))
            .departmentId(getNullableLongValue(value, StoreOpenStoreCloseDate_.SHOP, LocationDepartment_.ID))
            .openDate(getNullableDateValue(value, StoreOpenStoreCloseDate_.MANUAL_OPEN_DATE))
            .closeDate(getNullableDateValue(value, StoreOpenStoreCloseDate_.MANUAL_CLOSE_DATE))
            .build()
    ),
    REPLENISHMENT_STRATEGY_MSC(
        ReplenishmentStrategyMSC.class, 17,
        "coll_prd_msc_shop_repl_str",
            value -> TpReplenishmentStrategyMSC.builder()
            .id(getNullableLongValue(value, ReplenishmentStrategyMSC_.ID))
            .collectionId(getNullableLongValue(value, ReplenishmentStrategyMSC_.COLLECTION, Collection_.ID))
            .startDayId(getNullableIdDateValue(value, ReplenishmentStrategyMSC_.START_DAY))
            .endDayId(getNullableIdDateValue(value, ReplenishmentStrategyMSC_.END_DAY))
            .colorId(getNullableLongValue(value, ReplenishmentStrategyMSC_.MODEL, MerchColorModel_.ID))
            .departmentId(getNullableLongValue(value, ReplenishmentStrategyMSC_.SHOP, LocationDepartment_.ID))
            .replStrategyId(getNullableLongValue(value, ReplenishmentStrategyMSC_.VALUE, ReplenishmentStrategy_.ID))
            .build()
    ),
    PLAN_LIMIT_MSC(
        PlanLimitMSC.class, 18,
        "coll_msc_shop_pt_exts_plan_limit",
            value -> TpPlanLimitMSC.builder()
            .id(getNullableLongValue(value, PlanLimitMSC_.ID))
            .collectionId(getNullableLongValue(value, PlanLimitMSC_.COLLECTION, Collection_.ID))
            .modelId(getNullableLongValue(value, PlanLimitMSC_.MODEL, MerchColorModel_.ID))
            .departmentId(getNullableLongValue(value, PlanLimitMSC_.SHOP, LocationDepartment_.ID))
            .extSizeId(getNullableLongValue(value, PlanLimitMSC_.EXT_SIZE, ScaleSize_.ID))
            .upaType(getNullableValue(value, JsonNode::asText, PlanLimitMSC_.PRESENTATION_TYPE))
            .planLimit(getNullableIntValue(value, PlanLimitMSC_.VALUE))
            .build()
    ),
    COEFFICIENT_UPPER_PLAN_LIMIT_MSC(
        CoefficientUpperPlanLimitMSC.class, 19,
        "coll_msc_shop_upper_plan_lim",
            value -> TpCoefficientUpperPlanLimitMSC.builder()
            .id(getNullableLongValue(value, CoefficientUpperPlanLimitMSC_.ID))
            .collectionId(getNullableLongValue(value, CoefficientUpperPlanLimitMSC_.COLLECTION, Collection_.ID))
            .modelId(getNullableLongValue(value, CoefficientUpperPlanLimitMSC_.MODEL, MerchColorModel_.ID))
            .departmentId(getNullableLongValue(value, CoefficientUpperPlanLimitMSC_.SHOP, LocationDepartment_.ID))
            .coefUpperPlan(getNullableIntValue(value, CoefficientUpperPlanLimitMSC_.VALUE))
            .build()
    ),
    REPLENISHMENT_STRATEGY_DSC(
        ReplenishmentStrategyDSC.class, 21,
        "coll_prd_dsc_shop_repl_str",
            value -> TpReplenishmentStrategyDSC.builder()
            .id(getNullableLongValue(value, ReplenishmentStrategyDSC_.ID))
            .collectionId(getNullableLongValue(value, ReplenishmentStrategyDSC_.COLLECTION, Collection_.ID))
            .startDayId(getNullableIdDateValue(value, ReplenishmentStrategyDSC_.START_DAY))
            .endDayId(getNullableIdDateValue(value, ReplenishmentStrategyDSC_.END_DAY))
            .colorId(getNullableLongValue(value, ReplenishmentStrategyDSC_.MODEL, DevColorModel_.ID))
            .departmentId(getNullableLongValue(value, ReplenishmentStrategyDSC_.SHOP, LocationDepartment_.ID))
            .replStrategyId(getNullableLongValue(value, ReplenishmentStrategyDSC_.VALUE, ReplenishmentStrategy_.ID))
            .build()
    ),
    PLAN_LIMIT_DSC(
        PlanLimitDSC.class, 22,
        "coll_dsc_shop_pt_exts_plan_limit",
            value -> TpPlanLimitDSC.builder()
            .id(getNullableLongValue(value, PlanLimitDSC_.ID))
            .collectionId(getNullableLongValue(value, PlanLimitDSC_.COLLECTION, Collection_.ID))
            .modelId(getNullableLongValue(value, PlanLimitDSC_.MODEL, DevColorModel_.ID))
            .departmentId(getNullableLongValue(value, PlanLimitDSC_.SHOP, LocationDepartment_.ID))
            .extSizeId(getNullableLongValue(value, PlanLimitDSC_.EXT_SIZE, ScaleSize_.ID))
            .upaType(getNullableValue(value, JsonNode::asText, PlanLimitDSC_.PRESENTATION_TYPE))
            .planLimit(getNullableIntValue(value, PlanLimitDSC_.VALUE))
            .build()
    ),
    COEFFICIENT_UPPER_PLAN_LIMIT_DSC(
        CoefficientUpperPlanLimitDSC.class, 24,
        "coll_dsc_shop_upper_plan_lim",
            value -> TpCoefficientUpperPlanLimitDSC.builder()
            .id(getNullableLongValue(value, CoefficientUpperPlanLimitDSC_.ID))
            .collectionId(getNullableLongValue(value, CoefficientUpperPlanLimitDSC_.COLLECTION, Collection_.ID))
            .modelId(getNullableLongValue(value, CoefficientUpperPlanLimitDSC_.MODEL, DevColorModel_.ID))
            .departmentId(getNullableLongValue(value, CoefficientUpperPlanLimitDSC_.SHOP, LocationDepartment_.ID))
            .coefUpperPlan(getNullableIntValue(value, CoefficientUpperPlanLimitDSC_.VALUE))
            .build()
    ),
    START_LIST_STORE_MSCS(
        StartListStoreMSCS.class, 25,
        "coll_mscs_shop_min_upa_inex_dat_strtl",
            value -> TpStartListStoreMSCS.builder()
            .id(getNullableLongValue(value, StartListStoreMSCS_.ID))
            .collectionId(getNullableLongValue(value, StartListStoreMSCS_.COLLECTION, Collection_.ID))
            .sizeId(getNullableLongValue(value, StartListStoreMSCS_.SIZE, MerchColorSize_.ID))
            .departmentId(getNullableLongValue(value, StartListStoreMSCS_.SHOP, LocationDepartment_.ID))
            .intakeDate(getNullableDateValue(value, StartListStoreMSCS_.INTAKE_DATE))
            .exitDate(getNullableDateValue(value, StartListStoreMSCS_.EXIT_DATE))
            .minUpa(getNullableIntValue(value, StartListStoreMSCS_.MIN_UPA))
            .build()
    ),
    START_LIST_STORE_MSC(
        StartListStoreMSC.class, 27,
        "coll_msc_shop_min_upa_inex_dat_strtl",
            value -> TpStartListStoreMSC.builder()
            .id(getNullableLongValue(value, StartListStoreMSC_.ID))
            .collectionId(getNullableLongValue(value, StartListStoreMSC_.COLLECTION, Collection_.ID))
            .modelId(getNullableLongValue(value, StartListStoreMSC_.MODEL, MerchColorModel_.ID))
            .departmentId(getNullableLongValue(value, StartListStoreMSC_.SHOP, LocationDepartment_.ID))
            .intakeDate(getNullableDateValue(value, StartListStoreMSC_.INTAKE_DATE))
            .exitDate(getNullableDateValue(value, StartListStoreMSC_.EXIT_DATE))
            .minUpa(getNullableIntValue(value, StartListStoreMSC_.MIN_UPA))
            .build()
    ),
    START_LIST_STORE_DSCS(
        StartListStoreDSCS.class, 29,
        "coll_dscs_shop_min_upa_inex_dat_strtl",
            value -> TpStartListStoreDSCS.builder()
            .id(getNullableLongValue(value, StartListStoreDSCS_.ID))
            .collectionId(getNullableLongValue(value, StartListStoreDSCS_.COLLECTION, Collection_.ID))
            .sizeId(getNullableLongValue(value, StartListStoreDSCS_.SIZE, MerchColorSize_.ID))
            .departmentId(getNullableLongValue(value, StartListStoreDSCS_.SHOP, LocationDepartment_.ID))
            .intakeDate(getNullableDateValue(value, StartListStoreDSCS_.INTAKE_DATE))
            .exitDate(getNullableDateValue(value, StartListStoreDSCS_.EXIT_DATE))
            .minUpa(getNullableIntValue(value, StartListStoreDSCS_.MIN_UPA))
            .build()
    ),
    START_LIST_STORE_DSC(
        StartListStoreDSC.class, 31,
        "coll_dsc_shop_min_upa_inex_dat_strtl",
            value -> TpStartListStoreDSC.builder()
            .id(getNullableLongValue(value, StartListStoreDSC_.ID))
            .collectionId(getNullableLongValue(value, StartListStoreDSC_.COLLECTION, Collection_.ID))
            .modelId(getNullableLongValue(value, StartListStoreDSC_.MODEL, MerchColorModel_.ID))
            .departmentId(getNullableLongValue(value, StartListStoreDSC_.SHOP, LocationDepartment_.ID))
            .intakeDate(getNullableDateValue(value, StartListStoreDSC_.INTAKE_DATE))
            .exitDate(getNullableDateValue(value, StartListStoreDSC_.EXIT_DATE))
            .minUpa(getNullableIntValue(value, StartListStoreDSC_.MIN_UPA))
            .build()
    ),
    MIN_UPA_MSCS(
        MinUpaMSCS.class, 33,
        "coll_mscs_shop_category_pt_min_upa",
            value -> TpMinUpaMSCS.builder()
            .id(getNullableLongValue(value, MinUpaMSCS_.ID))
            .collectionId(getNullableLongValue(value, MinUpaMSCS_.COLLECTION, Collection_.ID))
            .sizeId(getNullableLongValue(value, MinUpaMSCS_.SIZE, MerchColorModel_.ID))
            .departmentId(getNullableLongValue(value, MinUpaMSCS_.SHOP, LocationDepartment_.ID))
            .categoryId(getNullableLongValue(value, MinUpaMSCS_.CATEGORY, Category_.ID))
            .updType(getNullableValue(value, JsonNode::asText, MinUpaMSCS_.PRESENTATION_TYPE))
            .minUpa(getNullableIntValue(value, MinUpaMSCS_.UPA))
            .build()
    ),
    MIN_UPA_DSCS(
        MinUpaDSCS.class, 34,
        "coll_dscs_shop_category_pt_min_upa",
            value -> TpMinUpaDSCS.builder()
            .id(getNullableLongValue(value, MinUpaDSCS_.ID))
            .collectionId(getNullableLongValue(value, MinUpaDSCS_.COLLECTION, Collection_.ID))
            .sizeId(getNullableLongValue(value, MinUpaDSCS_.SIZE, DevColorModel_.ID))
            .departmentId(getNullableLongValue(value, MinUpaDSCS_.SHOP, LocationDepartment_.ID))
            .categoryId(getNullableLongValue(value, MinUpaDSCS_.CATEGORY, Category_.ID))
            .updType(getNullableValue(value, JsonNode::asText, MinUpaDSCS_.PRESENTATION_TYPE))
            .minUpa(getNullableIntValue(value, MinUpaDSCS_.UPA))
            .build()
    ),
    MANUAL_DAY_WGHT(
        ManualDayWeight.class, 35,
        "sop_shop_dayid_mdwght",
            value -> TpManualDayWeight.builder()
            .id(getNullableLongValue(value, ManualDayWeight_.ID))
            .nodeId(getNullableLongValue(value, ManualDayWeight_.PLANNING_ROW, RDHierarchyGeneral_.ID, "id"))
            .levelNum(getNullableLongValue(value, ManualDayWeight_.PLANNING_ROW, RDHierarchyGeneral_.ID, "level"))
            .departmentId(getNullableLongValue(value, ManualDayWeight_.SHOP, LocationDepartment_.ID))
            .dayId(getNullableIdDateValue(value, ManualDayWeight_.DAY))
            .value(getNullableDoubleValue(value, ManualDayWeight_.VALUE))
            .build()
    ),
    PLAN_3D_VAL(
        Plan3DVal.class, 36,
        "coll_sop_shop_val",
            value -> TpPlan3DVal.builder()
            .id(getNullableLongValue(value, Plan3DVal_.ID))
            .collectionId(getNullableLongValue(value, Plan3DVal_.COLLECTION, Collection_.ID))
            .nodeId(getNullableLongValue(value, Plan3DVal_.PLANNING_ROW, RDHierarchyGeneral_.ID))
            .departmentId(getNullableLongValue(value, Plan3DVal_.SHOP, LocationDepartment_.ID))
            .val(getNullableIntValue(value, Plan3DVal_.VALUE))
            .build()
    ),
    PLAN_3D_MTPL(
        Plan3DMtpl.class, 37,
        "coll_sop_shop_mult",
            value -> TpPlan3DMtpl.builder()
            .id(getNullableLongValue(value, Plan3DMtpl_.ID))
            .collectionId(getNullableLongValue(value, Plan3DMtpl_.COLLECTION, Collection_.ID))
            .nodeId(getNullableLongValue(value, Plan3DMtpl_.PLANNING_ROW, RDHierarchyGeneral_.ID))
            .departmentId(getNullableLongValue(value, Plan3DMtpl_.SHOP, LocationDepartment_.ID))
            .mult(getNullableDoubleValue(value, Plan3DMtpl_.MULT))
            .build()
    ),
    LC_MIN_UPA_MSC(
        LcMinUpaMSC.class, 38,
        "coll_msc_shop_category_pt_ospertyp_val",
            value -> TpLcMinUpaMSC.builder()
            .id(getNullableLongValue(value, LcMinUpaMSC_.ID))
            .collectionId(getNullableLongValue(value, LcMinUpaMSC_.COLLECTION, Collection_.ID))
            .modelId(getNullableLongValue(value, LcMinUpaMSC_.MODEL, MerchColorModel_.ID))
            .categoryId(getNullableLongValue(value, LcMinUpaMSC_.CATEGORY, Category_.ID))
            .departmentId(getNullableLongValue(value, LcMinUpaMSC_.SHOP, LocationDepartment_.ID))
            .periodTypeId(getNullableLongValue(value, LcMinUpaMSC_.PERIOD_TYPE, OsLifecycleStage_.ID))
            .updType(getNullableValue(value, JsonNode::asText, LcMinUpaMSC_.PRESENTATION_TYPE))
            .val(getNullableIntValue(value, LcMinUpaMSC_.VALUE))
            .build()
    ),
    LC_MIN_UPA_DSC(
        LcMinUpaDSC.class, 39,
        "coll_dsc_shop_category_pt_ospertyp_val",
            value -> TpLcMinUpaDSC.builder()
            .id(getNullableLongValue(value, LcMinUpaDSC_.ID))
            .collectionId(getNullableLongValue(value, LcMinUpaDSC_.COLLECTION, Collection_.ID))
            .modelId(getNullableLongValue(value, LcMinUpaDSC_.MODEL, DevColorModel_.ID))
            .categoryId(getNullableLongValue(value, LcMinUpaDSC_.CATEGORY, Category_.ID))
            .departmentId(getNullableLongValue(value, LcMinUpaDSC_.SHOP, LocationDepartment_.ID))
            .periodTypeId(getNullableLongValue(value, LcMinUpaDSC_.PERIOD_TYPE, OsLifecycleStage_.ID))
            .updType(getNullableValue(value, JsonNode::asText, LcMinUpaDSC_.PRESENTATION_TYPE))
            .val(getNullableIntValue(value, LcMinUpaDSC_.VALUE))
            .build()
    ),
    LC_MIN_UPA_MSCS(
        LcMinUpaMSCS.class, 40,
        "coll_mscs_shop_category_pt_ospertyp_val",
            value -> TpLcMinUpaMSCS.builder()
            .id(getNullableLongValue(value, LcMinUpaMSCS_.ID))
            .collectionId(getNullableLongValue(value, LcMinUpaMSCS_.COLLECTION, Collection_.ID))
            .sizeId(getNullableLongValue(value, LcMinUpaMSCS_.SIZE, MerchColorSize_.ID))
            .categoryId(getNullableLongValue(value, LcMinUpaMSCS_.CATEGORY, Category_.ID))
            .departmentId(getNullableLongValue(value, LcMinUpaMSCS_.SHOP, LocationDepartment_.ID))
            .periodTypeId(getNullableLongValue(value, LcMinUpaMSCS_.PERIOD_TYPE, OsLifecycleStage_.ID))
            .updType(getNullableValue(value, JsonNode::asText, LcMinUpaMSCS_.PRESENTATION_TYPE))
            .val(getNullableIntValue(value, LcMinUpaMSCS_.VALUE))
            .build()
    ),
    LC_MIN_UPA_DSCS(
        LcMinUpaDSCS.class, 41,
        "coll_dscs_shop_category_pt_ospertyp_val",
            value -> TpLcMinUpaDSCS.builder()
            .id(getNullableLongValue(value, LcMinUpaDSCS_.ID))
            .collectionId(getNullableLongValue(value, LcMinUpaDSCS_.COLLECTION, Collection_.ID))
            .sizeId(getNullableLongValue(value, LcMinUpaDSCS_.SIZE, MerchColorSize_.ID))
            .categoryId(getNullableLongValue(value, LcMinUpaDSCS_.CATEGORY, Category_.ID))
            .departmentId(getNullableLongValue(value, LcMinUpaDSCS_.SHOP, LocationDepartment_.ID))
            .periodTypeId(getNullableLongValue(value, LcMinUpaDSCS_.PERIOD_TYPE, OsLifecycleStage_.ID))
            .updType(getNullableValue(value, JsonNode::asText, LcMinUpaDSCS_.PRESENTATION_TYPE))
            .val(getNullableIntValue(value, LcMinUpaDSCS_.VALUE))
            .build()
    ),
    LC_DATES_DSC(
        LcDatesDSC.class, 42,
        "coll_dsc_shop_ospertypdt",
            value -> TpLcDatesDSC.builder()
            .id(getNullableLongValue(value, LcDatesDSC_.ID))
            .collectionId(getNullableLongValue(value, LcDatesDSC_.COLLECTION, Collection_.ID))
            .modelId(getNullableLongValue(value, LcDatesDSC_.MODEL, DevColorModel_.ID))
            .departmentId(getNullableLongValue(value, LcDatesDSC_.SHOP, LocationDepartment_.ID))
            .intakeDate(getNullableDateValue(value, LcDatesDSC_.INTAKE_DATE))
            .markdownDate(getNullableDateValue(value, LcDatesDSC_.MARKDOWN_DATE))
            .suspendedDate(getNullableDateValue(value, LcDatesDSC_.SUSPENDED_DATE))
            .saleDate(getNullableDateValue(value, LcDatesDSC_.SALE_DATE))
            .exitDate(getNullableDateValue(value, LcDatesDSC_.EXIT_DATE))
            .build()
    ),
    LC_DATES_MSC(
        LcDatesMSC.class, 43,
        "coll_msc_shop_ospertypdt",
            value -> TpLcDatesMSC.builder()
            .id(getNullableLongValue(value, LcDatesMSC_.ID))
            .collectionId(getNullableLongValue(value, LcDatesMSC_.COLLECTION, Collection_.ID))
            .modelId(getNullableLongValue(value, LcDatesMSC_.MODEL, DevColorModel_.ID))
            .departmentId(getNullableLongValue(value, LcDatesMSC_.SHOP, LocationDepartment_.ID))
            .intakeDate(getNullableDateValue(value, LcDatesMSC_.INTAKE_DATE))
            .markdownDate(getNullableDateValue(value, LcDatesMSC_.MARKDOWN_DATE))
            .suspendedDate(getNullableDateValue(value, LcDatesMSC_.SUSPENDED_DATE))
            .saleDate(getNullableDateValue(value, LcDatesMSC_.SALE_DATE))
            .exitDate(getNullableDateValue(value, LcDatesMSC_.EXIT_DATE))
            .build()
    ),
    REPL_END_DATE_MSCS(
        ReplEndDateMSCS.class, 44,
        "coll_mscs_shop_repl_end_dat",
            value -> TpReplEndDateMSCS.builder()
            .id(getNullableLongValue(value, ReplEndDateMSCS_.ID))
            .collectionId(getNullableLongValue(value, ReplEndDateMSCS_.COLLECTION, Collection_.ID))
            .sizeId(getNullableLongValue(value, ReplEndDateMSCS_.SIZE, MerchColorSize_.ID))
            .departmentId(getNullableLongValue(value, ReplEndDateMSCS_.SHOP, LocationDepartment_.ID))
            .replEndDate(getNullableDateValue(value, ReplEndDateMSCS_.REPL_END_DATE))
            .build()
    ),
    MSC_SHOP_WSS_PARAMS(
        CoefficientWSSAllocationMSC.class, 45,
        "coll_msc_shop_wss_params",
            value -> TpCoefficientWSSAllocationMSC.builder()
            .id(getNullableLongValue(value, CoefficientWSSAllocationMSC_.ID))
            .collectionId(getNullableLongValue(value, CoefficientWSSAllocationMSC_.COLLECTION, Collection_.ID))
            .modelId(getNullableLongValue(value, CoefficientWSSAllocationMSC_.MODEL, MerchColorModel_.ID))
            .departmentId(getNullableLongValue(value, CoefficientWSSAllocationMSC_.SHOP, LocationDepartment_.ID))
            .percentSafStock(getNullableDoubleValue(value, CoefficientWSSAllocationMSC_.SAFETY_STOCK_PERCENT))
            .minWeekSafStock(getNullableDoubleValue(value, CoefficientWSSAllocationMSC_.MIN_WEEK_SAFETY_STOCK))
            .maxWeekSafStock(getNullableDoubleValue(value, CoefficientWSSAllocationMSC_.MAX_WEEK_SAFETY_STOCK))
            .periodWeekSafStock(getNullableDoubleValue(value, CoefficientWSSAllocationMSC_.PERIOD_WEEK_SAFETY_STOCK))
            .build()
    ),
    DSC_SHOP_WSS_PARAMS(
        CoefficientWSSAllocationDSC.class, 46,
        "coll_dsc_shop_wss_params",
            value -> TpCoefficientWSSAllocationDSC.builder()
            .id(getNullableLongValue(value, CoefficientWSSAllocationDSC_.ID))
            .collectionId(getNullableLongValue(value, CoefficientWSSAllocationDSC_.COLLECTION, Collection_.ID))
            .modelId(getNullableLongValue(value, CoefficientWSSAllocationDSC_.MODEL, DevColorModel_.ID))
            .departmentId(getNullableLongValue(value, CoefficientWSSAllocationDSC_.SHOP, LocationDepartment_.ID))
            .percentSafStock(getNullableDoubleValue(value, CoefficientWSSAllocationDSC_.SAFETY_STOCK_PERCENT))
            .minWeekSafStock(getNullableDoubleValue(value, CoefficientWSSAllocationDSC_.MIN_WEEK_SAFETY_STOCK))
            .maxWeekSafStock(getNullableDoubleValue(value, CoefficientWSSAllocationDSC_.MAX_WEEK_SAFETY_STOCK))
            .periodWeekSafStock(getNullableDoubleValue(value, CoefficientWSSAllocationDSC_.PERIOD_WEEK_SAFETY_STOCK))
            .build()
    ),
    DSC_VAL(
        DSCVal.class, 47,
        "coll_dsc_val",
            value -> TpDSCVal.builder()
            .id(getNullableLongValue(value, DSCVal_.ID))
            .collectionId(getNullableLongValue(value, DSCVal_.COLLECTION, Collection_.ID))
            .modelId(getNullableLongValue(value, DSCVal_.MODEL, DevColorModel_.ID))
            .val(getNullableIntValue(value, DSCVal_.VALUE))
            .build()
    ),
    MANUAL_QUAL_GOODS_MP(
        ManualQualGoodsMp.class, 48,
        "week_msc_shop_frcst",
            value -> TpWeekStoreForecastMSC.builder()
            .id(getNullableLongValue(value, WeekStoreForecastMSC_.ID))
            .weekId(getNullableIdDateValue(value, WeekStoreForecastMSC_.WEEK))
            .modelId(getNullableLongValue(value, WeekStoreForecastMSC_.MODEL, MerchColorModel_.ID))
            .departmentId(getNullableLongValue(value, WeekStoreForecastMSC_.SHOP, LocationDepartment_.ID))
            .manFrcst(getNullableDoubleValue(value, WeekStoreForecastMSC_.VALUE))
            .build()
    ),
    LC_SALES_RGN_BUSINESS_ST_REPLACE_TYPE_VOLUME_DSCS(
        LcSlsrgnBsnssStReptypeDSCS.class, 49,
        "dscs_coll_salesrgn_dtmats_ps_volume",
            value -> TpLcSlsrgnBsnssStReptypeDSCS.builder()
            .id(getNullableLongValue(value, LcSlsrgnBsnssStReptypeDSCS_.ID))
            .sizeId(getNullableLongValue(value, LcSlsrgnBsnssStReptypeDSCS_.SIZE, DevColorSize_.ID))
            .countryGroup(getNullableLongValue(value, LcSlsrgnBsnssStReptypeDSCS_.COUNTRY_GROUP, CountryGroup_.ID))
            .collectionId(getNullableLongValue(value, LcSlsrgnBsnssStReptypeDSCS_.COLLECTION, Collection_.ID))
            .divisionSourceId(getNullableLongValue(value, LcSlsrgnBsnssStReptypeDSCS_.DIVISION_SOURCE, DivisionTMA_.ID))
            .divisionTargetId(getNullableLongValue(value, LcSlsrgnBsnssStReptypeDSCS_.DIVISION_TARGET, DivisionTMA_.ID))
            .replaceType(getNullableLongValue(value, LcSlsrgnBsnssStReptypeDSCS_.REPLACE_TYPE))
            .psVolume(getNullableDoubleValue(value, LcSlsrgnBsnssStReptypeDSCS_.PS_VOLUME))
            .build()
    );

    private final Class<?> rowClass;
    private final long order;
    private final String dbType;
    private final Function<JsonNode, ITpAdjustmentRow> mapper;

    public ITpAdjustmentRow mapToStruct(JsonNode node) {
        return this.mapper.apply(node);
    }

    public static AdjustmentType findByClass(Class<?> clazz) {
        return Arrays.stream(values()).filter(i -> clazz.equals(i.rowClass)).findFirst().orElse(null);
    }

    public static AdjustmentType findById(Long id) {
        return Arrays.stream(values()).filter(i -> i.order == id).findFirst().orElse(null);
    }

    public static boolean hasById(Long id) {
        return Arrays.stream(values()).anyMatch(i -> i.order == id);
    }

    private static Long getNullableLongValue(JsonNode node, String... path) {
        return getNullableValue(node, val -> toValue(val, JsonNode::asLong), path);
    }

    private static Integer getNullableIntValue(JsonNode node, String... path) {
        return getNullableValue(node, val -> toValue(val, JsonNode::asInt), path);
    }

    private static Double getNullableDoubleValue(JsonNode node, String... path) {
        return getNullableValue(node, val -> toValue(val, JsonNode::asDouble), path);
    }

    private static LocalDate getNullableDateValue(JsonNode node, String... path) {
        return getNullableValue(node, value -> {
            if (value.isNull()) {
                return null;
            } else {
                return ConvertUtil.getDateValue(value.asText());
            }
        }, path);
    }

    private static Integer getNullableBoolValue(JsonNode node, String... path) {
        var value = getNullableValue(node, JsonNode::asBoolean, path);
        return nonNull(value) && Boolean.TRUE == value ? 1 : 0;
    }

    private static Long getNullableIdDateValue(JsonNode node, String... path) {
        String week = getNullableValue(node, JsonNode::asText, path);
        if (nonNull(week)) {
            return Long.parseLong(LocalDate.parse(week, DATE_FORMAT).format(DB_CALENDAR_ID_FORMAT));
        }
        return null;
    }

    private static <T> T toValue(JsonNode node, Function<JsonNode, T> mapFunc) {
        if (node.isNull() || (node.isPojo() && isNull(((POJONode) node).getPojo()))) {
            return null;
        }

        return mapFunc.apply(node);
    }
}
