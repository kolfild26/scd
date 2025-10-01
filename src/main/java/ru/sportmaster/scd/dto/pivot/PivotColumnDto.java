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
@Schema(description = "Колонка сводной таблицы")
public class PivotColumnDto {
    @Schema(
        name = "key",
        description = "Ключ колонки в данных"
    )
    private String key;
    @Schema(
        name = "title",
        description = "Название колонки для отображения"
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalizedProperty title;
    @Schema(
        name = "hidden",
        description = "Признак скрытия колонки"
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean hidden;
    @Schema(
        name = "children",
        description = "Вложенные колонки"
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<PivotColumnDto> children;
    @Schema(
        name = "columnFilterType",
        description = """
            Тип колонки для формирования фильтров
                        
            `0` - Строковый тип,
            `1` - Числовой тип,
            `2` - Тип Date,
            `3` - Тип Timestamp
            """
    )
    @JsonInclude
    private Integer columnFilterType;
    @Schema(
        name = "isFixed",
        description = "Относится поле к левой части сводной"
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isFixed;
}

