package ru.sportmaster.scd.dto.view;

import static java.util.Optional.ofNullable;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import ru.sportmaster.scd.ui.view.type.ICondition;

@Builder
@Data
public class SelectionRequestDto {
    @Schema(
        description = "View объекта",
        name = "view",
        requiredMode = Schema.RequiredMode.REQUIRED,
        example = "LocationDepartment"
    )
    private String view;
    @Schema(
        description = "Операция выбора",
        name = "command",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private SelectionCommand command;
    @Schema(
        description = "Признак множественного выбора",
        name = "multiselect"
    )
    @Builder.Default
    private Boolean multiselect = true;
    @Schema(
        description = "Список идентификаторов",
        name = "ids",
        example = "[1,2,3,4,5]"
    )
    private List<Object> ids;
    @Schema(
        description = "Список условий для выбора",
        name = "conditions"
    )
    private List<ICondition> conditions;

    public static SelectionRequestDto createCommand(SelectionCommand command) {
        return SelectionRequestDto.builder().command(command).build();
    }

    public boolean isMultiselect() {
        return ofNullable(multiselect).orElse(true);
    }
}
