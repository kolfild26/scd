package ru.sportmaster.scd.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AlgorithmTaskDto {
    @Schema(
        description = "Идентификатор описания алгоритма",
        name = "algorithmDefinitionId",
        requiredMode = Schema.RequiredMode.REQUIRED,
        example = "1")
    private Long algorithmDefinitionId;
    @Schema(
        description = "Параметры запуска алгоритма",
        name = "params",
        example = """
            {
                "p_id_partition_div_tma":1,
                "p_calc_date": "20.07.2015",
                "p_collections_before": 1
            }
            """)
    private Map<String, Object> params;
    @Schema(
        description = "Признак остановки выполнения группы алгоритмов, в случае возникновения ошибки",
        name = "isStopOnError",
        example = "true")
    private boolean isStopOnError = true;
}
