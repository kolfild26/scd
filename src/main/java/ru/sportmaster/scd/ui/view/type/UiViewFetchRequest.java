package ru.sportmaster.scd.ui.view.type;

import static java.util.Optional.ofNullable;
import static ru.sportmaster.scd.consts.OpenApiConst.UI_VIEW_REF;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Sort;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос получения данных")
public class UiViewFetchRequest {
    @Schema(
        description = "Название view object",
        name = "view",
        ref = UI_VIEW_REF,
        example = "LocationDepartmentME"
    )
    @NotBlank
    private String view;
    @Schema(
        description = "Номер запрашиваемой страницы",
        name = "page",
        example = "0"
    )
    @Builder.Default
    private int page = 0;
    @Schema(
        description = "Размер запрашиваемой страницы",
        name = "size",
        example = "10"
    )
    @Builder.Default
    private int size = 1000;
    @Schema(
        description = "Получение всех данных, при передаче `true`, параметры `page` и `size` игнорируются",
        name = "full"
    )
    @Builder.Default
    private Boolean full = false;
    @Schema(
        description = "Дополнительные условия выбора из mem entity. "
            + "Можно применить только к view с полями `shop` или `ware`",
        name = "memSelections",
        example = "[{\"view\": \"LocationDepartmentME\", \"field\": \"shop.id\", \"formUuid\": \"xxxyyy\"}]"
    )
    private List<UIViewMemSelection> memSelections;
    @Schema(
        description = "Список условий запроса. Доступные поля для условий можно посмотреть в блоке Schema для view",
        name = "conditions",
        example = "[{\"join\":\"OR\", \"conditions\": [{\"field\":\"business.name\","
            + " \"operation\":\"EQ\", \"value\":\"Спорт\"}]}]"
    )
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ICondition> conditions;
    @Schema(
        description = "Сортировка строк. Доступные поля для сортировок можно посмотреть в блоке Schema для view",
        name = "sort",
        example = "{\"id\":\"DESC\"}"
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, Sort.Direction> sort = new TreeMap<>();

    @JsonIgnore
    public long getOffset() {
        return (long) page * (long) size;
    }

    public boolean isFull() {
        return ofNullable(full).orElse(false);
    }
}
