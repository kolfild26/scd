package ru.sportmaster.scd.dto.mementity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sportmaster.scd.dto.SelectionStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemEntitySelectDto<K, T> {
    @Schema(
        name = "firstValue",
        description = "Первая выбранная строка",
        example = "{\"id\":1,\"value\":\"firstMatch\"}"
    )
    private T firstValue;
    @Schema(
        name = "total",
        description = "Всего выбранных строк"
    )
    private int total;
    @Schema(
        name = "full",
        description = "Признак выбранны ли все записи"
    )
    private boolean full;
    @Schema(
        name = "selectionStatus",
        description = "Статус выбора"
    )
    private SelectionStatus selectionStatus;
    @JsonIgnore
    private K firstValueKey;
}
