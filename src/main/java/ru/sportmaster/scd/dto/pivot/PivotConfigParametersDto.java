package ru.sportmaster.scd.dto.pivot;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PivotConfigParametersDto {
    @Schema(
        description = "Список колонок для построения иерархии, с признаком выбранности",
        name = "hierarchy",
        example = "[{\"key\":\"wareGroup\",\"isSelected\":true,\"label\":\"Товарнаягруппа\"},"
            + "{\"key\":\"category\",\"isSelected\":true,\"label\":\"Категория\"},"
            + "{\"key\":\"colorModel\",\"isSelected\":true,\"label\":\"Цветомодель\"},"
            + "{\"key\":\"colorSize\",\"isSelected\":true,\"label\":\"Цветоразмер\"},"
            + "{\"key\":\"country\",\"isSelected\":true,\"label\":\"Страна\"},"
            + "{\"key\":\"region\",\"isSelected\":true,\"label\":\"Регион\"},"
            + "{\"key\":\"city\",\"isSelected\":true,\"label\":\"Город\"},"
            + "{\"key\":\"shop\",\"isSelected\":true,\"label\":\"Магазин\"}]"
    )
    private List<PivotConfigHierarchyItem> hierarchy;
    @Schema(
        description = "Список значений фильтров сводной",
        name = "filters",
        example = "{\"COLLECTION\":[11070299],\"Ware\":[5734800299,5734810299],\"Shop\":[12060299,12080299]}"
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, List<Long>> filters;
    @Schema(
        description = "Сортировка строк сводной",
        name = "sort",
        example = "{\"id\":\"DESC\"}"
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, Sort.Direction> sort = new TreeMap<>();
    @Schema(
        description = "Условия отбора сводной в формате JSON",
        name = "conditions",
        example = "{\"columnName\": \"A\", \"condition\": \"=\", \"value\": [1]}"
    )
    private String conditions;
}
