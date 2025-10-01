package ru.sportmaster.scd.dto.adjustment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("unused")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdjustmentDownloadRequestDto {
    @Schema(
        description = "Тип корректировки",
        name = "typeId"
    )
    private Long typeId;
    @Schema(
        description = "Идентификатор бизнеса",
        name = "businessId"
    )
    private Long businessId;
    @Schema(
        description = "Идентификатор документа",
        name = "documentId"
    )
    private Long documentId;

    @JsonIgnore
    public boolean isTemplate() {
        return Objects.isNull(documentId);
    }
}
