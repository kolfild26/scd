package ru.sportmaster.scd.dto.pivot;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sportmaster.scd.ui.view.type.LocalizedProperty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на формирование сводной")
public class PivotConfigDto {
    @Schema(
        description = "Список уровней иерархии сводной. Для иерархического вида",
        name = "levels",
        example = "[\"Сезон\", \"Неделя\"]"
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<LocalizedProperty> levels;
    @Schema(
        description = "Список колонок сводной",
        name = "columns"
    )
    private List<PivotColumnDto> columns;
    @Schema(
        description = "Список параметров конфигурации сводной",
        name = "parameters"
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PivotConfigParametersDto parameters;
    @Schema(
        description = "Общее количество строк сводной",
        name = "total",
        example = "1000000"
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long total;
    @Schema(
        description = "Признак наличия сохраненной конфигурации",
        name = "isEmpty"
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isEmpty;
}
