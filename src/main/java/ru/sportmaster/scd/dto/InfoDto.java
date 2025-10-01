package ru.sportmaster.scd.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class InfoDto {
    @Schema(
        description = "Номер версии приложения",
        name = "version"
    )
    private String version;
    @Schema(
        description = "Номер сборки приложения",
        name = "build"
    )
    private String build;
    @Schema(
        description = "Текущее окружение запуска",
        name = "env"
    )
    private String env;
}
