package ru.sportmaster.scd.ui.view.dictionary;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import ru.sportmaster.scd.ui.view.type.LocalizedProperty;

@Data
@Builder
@Schema(description = "Данные строки словаря")
public class DictionaryItem {
    @Schema(
        name = "id",
        description = "Идентификатор записи словаря",
        example = "1"
    )
    private Long id;
    @Schema(
        name = "label",
        description = "Название записи словаря",
        example = "Dic Label 1"
    )
    private LocalizedProperty label;
    @Schema(
        name = "value",
        description = "Значение записи словаря",
        example = "3422112"
    )
    private Object value;
    @Schema(
        name = "children",
        description = "Вложенные записи (для словарей-деревьев)"
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<DictionaryItem> children;
}
