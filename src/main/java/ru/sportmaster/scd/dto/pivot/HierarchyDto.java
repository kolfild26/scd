package ru.sportmaster.scd.dto.pivot;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sportmaster.scd.service.view.HierarchyResolver;
import ru.sportmaster.scd.ui.view.annotation.CustomView;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@CustomView(name = "Hierarchy", resolver = HierarchyResolver.class)
public class HierarchyDto {
    @Schema(
        name = "id",
        description = "Идентификатор иерархии"
    )
    private Long id;
    @Schema(
        name = "name",
        description = "Название иерархии"
    )
    private String name;
    @Schema(
        name = "children",
        description = "Вложенные иерархии"
    )
    @Builder.Default
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<HierarchyDto> children = new ArrayList<>();
}
