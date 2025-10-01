package ru.sportmaster.scd.dto.view;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UiViewSearchResponseDto {
    @Schema(
        description = "Позиция первой найденной строки на странице",
        name = "firstIndex",
        example = "1"
    )
    private Integer firstIndex;
    @Schema(
        description = "Количество записей на странице",
        name = "totalInPage",
        example = "3"
    )
    private Integer totalInPage;
    @Schema(
        description = "Список найденных позиций на странице",
        name = "matches",
        example = "[1,2,3]"
    )
    private List<Integer> matches;
}
