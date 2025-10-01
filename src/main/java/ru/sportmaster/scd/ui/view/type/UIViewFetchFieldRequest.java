package ru.sportmaster.scd.ui.view.type;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
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
@Schema(description = "Запрос получения данных для части полей. Поля не указанные в `fields` не передаются")
public class UIViewFetchFieldRequest extends UiViewFetchRequest {
    @Schema(
        description = "Список запрашиваемых полей view. Если необходимо получить только часть полей.",
        name = "fields",
        example = "[\"id\",\"name\",\"description\"]"
    )
    private List<String> fields;
}
