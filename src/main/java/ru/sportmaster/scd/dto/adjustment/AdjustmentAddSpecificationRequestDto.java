package ru.sportmaster.scd.dto.adjustment;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdjustmentAddSpecificationRequestDto {
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
        description = "Комментарий документа корректировки",
        name = "comment"
    )
    private String comment;
    @Schema(
        description = "Данные документа корректировки в формате json",
        name = "data"
    )
    private List<ObjectNode> data;
}
