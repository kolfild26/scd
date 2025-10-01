package ru.sportmaster.scd.dto.view;

import static java.util.Optional.ofNullable;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sportmaster.scd.ui.view.type.ICondition;
import ru.sportmaster.scd.ui.view.type.UIViewMemSelection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViewDownloadRequestDto {
    @Schema(
        description = "View объекта",
        name = "view",
        requiredMode = Schema.RequiredMode.REQUIRED,
        example = "LocationDepartment"
    )
    private String view;
    @Schema(
        description = "Дополнительные условия выбора из mem entity",
        name = "memSelections"
    )
    private List<UIViewMemSelection> memSelections;
    @Schema(
        description = "Список условий запроса",
        name = "conditions",
        example = "[{\"join\":\"OR\", \"conditions\": [{\"field\":\"business.name\","
            + " \"operation\":\"EQ\", \"value\":\"Спорт\"}]}]"
    )
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ICondition> conditions;
    @Schema(
        description = "Признак скачивания шаблона без строк",
        name = "template",
        example = "true"
    )
    private Boolean template;

    public boolean isTemplate() {
        return ofNullable(template).orElse(false);
    }
}
