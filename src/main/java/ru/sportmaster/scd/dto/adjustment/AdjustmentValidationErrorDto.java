package ru.sportmaster.scd.dto.adjustment;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.sportmaster.scd.ui.view.type.LocalizedProperty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"corrected"})
public class AdjustmentValidationErrorDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -7747547249787477252L;

    @Schema(
        description = "Номер строки, где обнаружена ошибка",
        name = "rowIndex"
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long rowIndex;
    @Schema(
        description = "Номер колонки, где обнаружена ошибка",
        name = "colIndex"
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long colIndex;
    @Schema(
        description = "Колонка, где обнаружена ошибка",
        name = "field"
    )
    private String field;
    @Schema(
        description = "Уникальный ключ ошибки валидации",
        name = "key"
    )
    private ValidationError key;
    @Builder.Default
    @Schema(
        description = "Признак исправленности ошибки",
        name = "corrected"
    )
    private Boolean corrected = false;

    @Schema(
        description = "Сообщение об ошибке",
        name = "message",
        example = "string"
    )
    public LocalizedProperty getMessage() {
        return new LocalizedProperty(
            String.format("%s.%s", ValidationError.class.getSimpleName(), key),
            key.name()
        );
    }
}
