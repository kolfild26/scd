package ru.sportmaster.scd.dto.pivot;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PivotValidationDataRequestDto {
    @Schema(
        description = "Идентификатор сводной",
        name = "pivotId"
    )
    private String pivotId;
    @Schema(
        description = "Колонка валидации",
        name = "column"
    )
    private String column;
}
