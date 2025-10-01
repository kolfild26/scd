package ru.sportmaster.scd.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDto {
    @Schema(
        description = "Код ошибки",
        name = "code",
        requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer code;
    @Schema(
        description = "Сообщение об ошибке",
        name = "message",
        requiredMode = Schema.RequiredMode.REQUIRED)
    private String message;
}
