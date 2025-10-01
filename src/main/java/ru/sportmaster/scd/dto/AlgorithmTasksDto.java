package ru.sportmaster.scd.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AlgorithmTasksDto {
    @Schema(
        description = "Список описаний алгоритмов на выполнение",
        name = "tasks",
        requiredMode = Schema.RequiredMode.REQUIRED)
    private List<AlgorithmTaskDto> tasks;
    @JsonIgnore
    private Long calcId;
}
