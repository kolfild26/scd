package ru.sportmaster.scd.service;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import ru.sportmaster.scd.dto.pivot.PivotConfigDto;
import ru.sportmaster.scd.dto.pivot.PivotConfigParametersDto;
import ru.sportmaster.scd.dto.pivot.PivotConfigStatusDto;
import ru.sportmaster.scd.dto.pivot.PivotDataRequest;
import ru.sportmaster.scd.dto.pivot.PivotDownloadRequestDto;
import ru.sportmaster.scd.dto.pivot.PivotFilterDto;
import ru.sportmaster.scd.dto.pivot.PivotPreparingFileDto;
import ru.sportmaster.scd.dto.pivot.PivotSearchDataRequestDto;
import ru.sportmaster.scd.dto.pivot.PivotValidationDataRequestDto;
import ru.sportmaster.scd.web.UserEnvironment;

public interface PivotService {
    PivotConfigDto getPivotConfig(String pivotId, UserEnvironment environment);

    void savePivotConfig(String pivotId, PivotConfigParametersDto parameters, UserEnvironment environment);

    PivotConfigStatusDto getSavePivotConfigStatus(String pivotId, UserEnvironment environment);

    List<Object[]> getPivotData(PivotDataRequest request, UserEnvironment environment);

    List<String> getPivotDataString(PivotDataRequest request, UserEnvironment environment);

    List<JsonNode> getPivotDataJson(PivotDataRequest request, UserEnvironment environment);

    List<Long> searchPivotRows(PivotSearchDataRequestDto request, UserEnvironment environment);

    List<Long> validatePivotRows(PivotValidationDataRequestDto request, UserEnvironment environment);

    String startPivotPreparingForDownload(PivotDownloadRequestDto request, UserEnvironment environment);

    PivotPreparingFileDto checkPreparePivotStatus(String uuid);

    byte[] downloadPivotFile(String uuid);

    List<PivotFilterDto> getFilterSets(String pivotId, UserEnvironment environment);

    void saveFilterSet(String pivotId, PivotFilterDto request, UserEnvironment environment);

    void deleteFilterSets(String pivotId, List<String> names, UserEnvironment environment);

    List<Object> getUniqFieldValues(String pivotId, String field, UserEnvironment environment);
}
