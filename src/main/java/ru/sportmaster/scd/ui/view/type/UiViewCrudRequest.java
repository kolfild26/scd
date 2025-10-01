package ru.sportmaster.scd.ui.view.type;

import static ru.sportmaster.scd.consts.OpenApiConst.UI_VIEW_REF;
import static ru.sportmaster.scd.consts.ParamNames.ID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = """
    Запрос на изменение данных. Необходимо указать:
    
    `view` - название view для изменения, `change` - тип изменения,
    И передать старые значение строки `oldValue` и новые `value`
    """, example = "{\"view\":\"SomeView\",\"type\":\"UPDATE\",\"oldValue\":"
    + "{\"id\":1,\"value\":\"old\"},\"value\":{\"id\":1,\"value\":\"new\"}}")
public class UiViewCrudRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 2128593100333080538L;

    @Schema(
        description = "Название view object",
        name = "view",
        ref = UI_VIEW_REF,
        example = "SomeView"
    )
    private String view;
    @Schema(
        description = "Тип изменения",
        name = "type",
        example = "UPDATE"
    )
    @NotBlank
    private ChangeType type;
    @Schema(
        description = "Старое значение строки view object",
        name = "oldValue",
        example = "{\"id\":10260299,\"name\":\"Гигаспорт\",\"isDeleted\":null}"
    )
    private ObjectNode oldValue;
    @Schema(
        description = "Текущее значение строки view object",
        name = "value",
        example = "{\"id\":10260299,\"name\":\"Мегаспорт\",\"isDeleted\":null}"
    )
    @NotBlank
    private ObjectNode value;

    @JsonIgnore
    public Long getId() {
        return Optional.ofNullable(value).filter(i -> i.has(ID)).map(i -> i.get(ID).asLong()).orElse(null);
    }

    @JsonIgnore
    public Long getOldId() {
        return Optional.ofNullable(oldValue).filter(i -> i.has(ID)).map(i -> i.get(ID).asLong()).orElse(null);
    }
}
