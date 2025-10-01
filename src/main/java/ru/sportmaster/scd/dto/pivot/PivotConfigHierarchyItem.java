package ru.sportmaster.scd.dto.pivot;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sportmaster.scd.ui.view.type.LocalizedProperty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PivotConfigHierarchyItem {
    @Schema(
        description = "Ключ выбранной колонки",
        name = "key",
        example = "country"
    )
    private String key;
    @Schema(
        description = "Признак выбрана ли колонка",
        name = "isSelected",
        example = "true"
    )
    private Boolean isSelected;

    @Schema(
        description = "Название выбранной колонки",
        name = "label",
        example = "Страна"
    )
    @Transient
    public LocalizedProperty getLabel() {
        return new LocalizedProperty(
            String.format("%s.%s", PivotConfigHierarchyItem.class.getSimpleName(), key),
            key
        );
    }
}
