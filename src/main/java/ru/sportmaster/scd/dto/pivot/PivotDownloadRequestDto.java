package ru.sportmaster.scd.dto.pivot;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PivotDownloadRequestDto {
    @Schema(
        description = "Идентификатор сводной",
        name = "pivotId"
    )
    private String pivotId;
    @Schema(
        description = "Формат результирующего файла",
        name = "type"
    )
    private DownloadType type;
    @Schema(
        description = "Ключи скрытых колонок. Какие колонки необходимо скрыть в файле",
        name = "hiddenColumnKeys",
        example = "[\"lsm_total\",\"lsm_business\"]"
    )
    private List<String> hiddenColumnKeys = new ArrayList<>();
    @Schema(
        description = "Ключи системных (скрытых) колонок. При передаче колонок они добавятся в файл",
        name = "dataColumnOptions",
        example = "[\"size_share\",\"size_profile_type_name\"]"
    )
    private List<String> dataColumnOptions = new ArrayList<>();
}
