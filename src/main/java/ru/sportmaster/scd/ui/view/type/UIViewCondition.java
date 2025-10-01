package ru.sportmaster.scd.ui.view.type;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UIViewCondition implements ICondition {
    @Schema(
        description = "Поле для применения условия",
        name = "field",
        example = "id"
    )
    private String field;
    @Schema(
        description = "Тип операции сравнения условия",
        name = "operation",
        example = "EQ"
    )
    private UIViewConditionOperation operation;
    @Schema(
        description = "Значения поля для сравнения",
        name = "value",
        example = "1"
    )
    private JsonNode value;

    @Override
    public boolean isGroup() {
        return false;
    }
}
