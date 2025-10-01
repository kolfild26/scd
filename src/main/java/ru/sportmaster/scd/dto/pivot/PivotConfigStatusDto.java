package ru.sportmaster.scd.dto.pivot;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sportmaster.scd.dto.Status;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Прогресс формирования сводной таблицы")
public class PivotConfigStatusDto {
    @Schema(
        name = "status",
        description = "Статус формирования сводной"
    )
    private Status status;
    @Schema(
        name = "progress",
        description = "Прогресс формирования сводной"
    )
    @Builder.Default
    private BigDecimal progress = BigDecimal.ZERO;
}
