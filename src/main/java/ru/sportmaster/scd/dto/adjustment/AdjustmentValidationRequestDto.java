package ru.sportmaster.scd.dto.adjustment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdjustmentValidationRequestDto {
    @Schema(
        description = "Тип загружаемого документа корректировки",
        name = "typeId"
    )
    private Long typeId;
    @Schema(
        description = "Идентификатор бизнеса для документа корректировки",
        name = "businessId"
    )
    private Long businessId;
    @Schema(
        description = "Идентификатор документа корректировки",
        name = "documentId"
    )
    private Long documentId;
    @Schema(
        description = "Список ошибок валидации",
        name = "errors",
        example = "[{\"rowIndex\":1,\"field\":\"intakeDate\","
            + "\"key\":\"FIELD_NOT_FOUND\",\"label\":\"Поле не найдено!\"}]"
    )
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Set<AdjustmentValidationErrorDto> errors;
    @Schema(
        description = "Данные документа корректировки в формате json",
        name = "data"
    )
    private List<ObjectNode> data;
}
