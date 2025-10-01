package ru.sportmaster.scd.ui.view.type;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sportmaster.scd.ui.view.annotation.CalendarEditorType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UIViewAttributeEditor {
    @Schema(
        name = "type",
        description = "Тип колонки для редактирования. Может содержать примитив или название view"
    )
    private String type;
    @Schema(
        name = "editor",
        description = "Ключ для редактирования. Куда необходимо сеттить изменения в данных"
    )
    private String name;
    @Schema(
        name = "dayOfWeek",
        description = "Установка значения дня данных с типом дата/время"
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer dayOfWeek;
    @Schema(
        name = "calendarType",
        description = "Установка типа календаря день/неделя"
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private CalendarEditorType calendarType;
}
