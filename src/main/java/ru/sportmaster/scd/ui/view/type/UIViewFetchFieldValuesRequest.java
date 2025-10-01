package ru.sportmaster.scd.ui.view.type;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос получения всех значений поля `path`, возможно сократить список при передаче "
    + "фильтра по значению `searchText`")
public class UIViewFetchFieldValuesRequest extends UiViewFetchRequest {
    @Schema(
        description = "Путь к полю view object",
        name = "path",
        example = "status.name"
    )
    @NotBlank
    private String path;
    @Schema(
        description = "Текст для фильтрации",
        name = "searchText"
    )
    private String searchText;
}
