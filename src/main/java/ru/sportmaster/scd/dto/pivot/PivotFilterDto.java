package ru.sportmaster.scd.dto.pivot;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PivotFilterDto {
    @Schema(
        description = "Название фильтр сета",
        name = "name",
        example = "Название фильтр сета"
    )
    private String name;
    @Schema(
        description = "Список значений фильтр-сета",
        name = "values",
        example = "{\"Ware\":[5734800299,5734810299],\"Shop\":[12060299,12080299]}"
    )
    private Map<String, List<Long>> values;
}
