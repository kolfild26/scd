package ru.sportmaster.scd.dto.pivot;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PivotDataRequest {
    @Schema(
        description = "Идентификатор сводной",
        name = "pivotId",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String pivotId;
    @Schema(
        description = "Номер страницы сводной",
        name = "page",
        requiredMode = Schema.RequiredMode.REQUIRED,
        example = "0"
    )
    private Integer page;
    @Schema(
        description = "Размер страницы сводной",
        name = "limit",
        requiredMode = Schema.RequiredMode.REQUIRED,
        example = "100"
    )
    private Integer limit;
    @Schema(
        description = "Список свернутых узлов (для иерархического вида)",
        name = "collapsedOrderRows",
        example = "[1,2,3]"
    )
    private String collapsedOrderRows;

    @JsonIgnore
    public long getOffset() {
        return (long) page * (long) limit;
    }
}
