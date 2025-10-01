package ru.sportmaster.scd.ui.view.type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Колонка view для отображения")
public class UiViewAttribute {
    @Schema(
        name = "type",
        description = "Тип колонки для отображения. Может содержать примитив или название view."
    )
    @NotBlank
    private String type;
    @Schema(
        name = "name",
        description = "Ключ колонки в данных, полученных через api - `fetchData`"
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;
    @Schema(
        name = "label",
        description = "Название колонки"
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalizedProperty label;
    @Schema(
        name = "editable",
        description = "Признак редактируемой колонки"
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean editable;
    @Schema(
        name = "required",
        description = "Признак обязательного заполнения колонки"
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean required;
    @Schema(
        name = "editor",
        description = "Данные для редактирования колонки"
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UIViewAttributeEditor editor;
    @Schema(
        name = "children",
        description = "Вложенные колонки"
    )
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<UiViewAttribute> children;

    @JsonIgnore
    private Integer order;
    @JsonIgnore
    private Class<?> javaType;
    @JsonIgnore
    private Class<?> sourceType;

    public boolean isEditable() {
        return getEditable() == null || getEditable();
    }
}
