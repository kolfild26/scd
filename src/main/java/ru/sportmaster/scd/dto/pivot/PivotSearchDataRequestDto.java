package ru.sportmaster.scd.dto.pivot;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "Запрос на поиск строк в сводной")
public class PivotSearchDataRequestDto {
    @Schema(
        description = "Идентификатор сводной",
        name = "pivotId"
    )
    private String pivotId;
    @Schema(
        description = "Текст для поиска по сводной",
        name = "text",
        example = "Текст для поиска"
    )
    private String text;
}
