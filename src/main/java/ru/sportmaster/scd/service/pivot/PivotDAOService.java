package ru.sportmaster.scd.service.pivot;

import com.fasterxml.jackson.databind.JsonNode;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;
import org.springframework.lang.NonNull;
import ru.sportmaster.scd.dto.pivot.PivotDownloadRequestDto;
import ru.sportmaster.scd.dto.pivot.PivotPreparingFileDto;
import ru.sportmaster.scd.entity.struct.TpPivotConfig;
import ru.sportmaster.scd.entity.struct.TpPivotConfigParams;
import ru.sportmaster.scd.entity.struct.TpPivotFilter;

public interface PivotDAOService {
    TpPivotConfig getPivotConfig(@NonNull String pivotName,
                                 @NonNull Long userId);

    TpPivotConfig savePivotConfig(@NonNull String pivotName,
                                  @NonNull Long userId,
                                  TpPivotConfigParams pivotConfigParams);

    List<TpPivotFilter> getFilterSets(@NonNull String pivotName,
                                      @NonNull Long userId);

    void saveFilterSet(@NonNull String pivotName,
                       @NonNull Long userId,
                       TpPivotFilter pivotFilter);

    void deleteFilterSets(@NonNull String pivotName,
                          @NonNull Long userId,
                          List<String> filterNames);

    List<Object[]> getPivotData(@NonNull String pivotName,
                                @NonNull Long userId,
                                @NonNull Long startRow,
                                @NonNull Long endRow);

    List<String> getPivotDataString(@NonNull String pivotName,
                                    @NonNull Long userId,
                                    @NonNull Long startRow,
                                    @NonNull Long endRow);

    List<JsonNode> getPivotDataJson(@NonNull String pivotName,
                                    @NonNull Long userId,
                                    @NonNull Long startRow,
                                    @NonNull Long endRow,
                                    String collapsedOrderRows);

    List<Long> searchPivotRows(@NonNull String pivotName,
                               @NonNull Long userId,
                               @NonNull String searchText);

    void downloadPivot(@NonNull PivotDownloadRequestDto request,
                       @NonNull Long userId,
                       @NonNull Consumer<PivotPreparingFileDto> updateStatus);

    BigDecimal getProgressPivot(@NonNull String pivotName,
                                @NonNull Long userId);

    List<Object> getUniqFieldValues(@NonNull String pivotName,
                                    @NonNull String fieldName,
                                    @NonNull Long userId);
}
