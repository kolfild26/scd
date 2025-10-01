package ru.sportmaster.scd.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PutAlgorithmsResponseDto {
    private static final String MAX_LONG = String.valueOf(Long.MAX_VALUE);

    @Schema(
        description = "Идентификатор группы алгоритмов",
        name = "groupAlgorithm",
        requiredMode = Schema.RequiredMode.REQUIRED,
        example = "9223372036854775807")
    private String groupAlgorithm;
    @Schema(
        description = "Список идентификаторов алгоритмов",
        name = "algorithms",
        requiredMode = Schema.RequiredMode.REQUIRED)
    private List<Long> algorithms;
}
