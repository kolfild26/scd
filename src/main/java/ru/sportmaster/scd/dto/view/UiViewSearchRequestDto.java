package ru.sportmaster.scd.dto.view;

import static ru.sportmaster.scd.consts.OpenApiConst.FORM_UUID_HEADER_DESCRIPTION;
import static ru.sportmaster.scd.consts.OpenApiConst.SESSION_UUID_HEADER_DESCRIPTION;
import static ru.sportmaster.scd.consts.OpenApiConst.UI_VIEW_REF;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Sort;
import ru.sportmaster.scd.ui.view.type.ICondition;
import ru.sportmaster.scd.ui.view.type.UIViewMemSelection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UiViewSearchRequestDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -218774008252213180L;

    @Schema(
        description = "Название view object",
        name = "view",
        ref = UI_VIEW_REF,
        example = "LocationDepartmentME"
    )
    private String view;
    @Schema(
        description = FORM_UUID_HEADER_DESCRIPTION,
        name = "formUuid"
    )
    private String formUuid;
    @Schema(
        description = SESSION_UUID_HEADER_DESCRIPTION,
        name = "sessionUuid"
    )
    private String sessionUuid;
    @Schema(
        description = "Текст для поиска",
        name = "searchText"
    )
    private String searchText;
    @Schema(
        description = "Номер запрашиваемой страницы",
        name = "page",
        example = "0"
    )
    private int page = 0;
    @Schema(
        description = "Размер запрашиваемой страницы",
        name = "size",
        example = "1000"
    )
    private int size = 1000;
    @Schema(
        description = "При новом запросе поиска педедается `true`, при поиска по страницам != 0 - `false`",
        name = "newSearch"
    )
    private boolean newSearch = false;

    @Schema(
        description = "Дополнительные условия выбора из mem entity. "
            + "Можно применить только к view с полями `shop` или `ware`",
        name = "memSelections",
        example = "[{\"view\": \"LocationDepartmentME\", \"field\": \"shop.id\", \"formUuid\": \"xxxyyy\"}]"
    )
    private transient List<UIViewMemSelection> memSelections;
    @Schema(
        description = "Список условий запроса. Доступные поля для условий можно посмотреть в блоке Schema для view",
        name = "conditions",
        example = "[{\"join\":\"OR\", \"conditions\": [{\"field\":\"business.name\","
            + " \"operation\":\"EQ\", \"value\":\"Спорт\"}]}]"
    )
    private transient List<ICondition> conditions;
    @Schema(
        description = "Сортировка строк. Доступные поля для сортировок можно посмотреть в блоке Schema для view",
        name = "sort",
        example = "{\"id\":\"DESC\"}"
    )
    private transient Map<String, Sort.Direction> sort = new TreeMap<>();

    @JsonIgnore
    public int getOffset() {
        return page * size;
    }
}
