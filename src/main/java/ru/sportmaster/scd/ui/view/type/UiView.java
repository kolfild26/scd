package ru.sportmaster.scd.ui.view.type;

import static ru.sportmaster.scd.consts.OpenApiConst.UI_VIEW_REF;
import static ru.sportmaster.scd.consts.ParamNames.IS_DELETED;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(builderMethodName = "hiddenBuilder")
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Данные view для отображеня. Включает список колонок - `attributes`")
public class UiView {
    @Schema(
        name = "type",
        description = "Название view",
        ref = UI_VIEW_REF,
        example = "LocationDepartmentME"
    )
    @NotBlank
    private String type;
    @JsonIgnore
    private String idField;
    @JsonIgnore
    private Class<?> javaType;
    @JsonIgnore
    private Class<?> idFieldType;
    @JsonIgnore
    private boolean recoverable;
    @JsonIgnore
    private boolean embeddedId;
    @Schema(
        name = "fetchType",
        description = "Тип скролла. `FULL` - получение всех данных. `INFINITY` - бесконечный скролл"
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private DataFetchType fetchType;
    @Schema(
        name = "attributes",
        description = "Список колонок для отображения"
    )
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<UiViewAttribute> attributes;

    @JsonIgnore
    public boolean isDeletable() {
        return Arrays.stream(javaType.getDeclaredFields()).anyMatch(i -> Objects.equals(i.getName(), IS_DELETED));
    }

    public static UiViewBuilder builder(Class<?> clazz) {
        return hiddenBuilder().type(clazz.getSimpleName()).javaType(clazz);
    }

    public static UiViewBuilder builder(String name, Class<?> clazz) {
        return hiddenBuilder().type(name).javaType(clazz);
    }
}
