package ru.sportmaster.scd.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@SuppressWarnings("unused")
public class LoginRequestDto {
    @Schema(
        description = "Имя пользователя",
        name = "username",
        requiredMode = Schema.RequiredMode.REQUIRED,
        example = "ADUsername")
    private String username;
    @Schema(
        description = "Пароль пользователя",
        name = "password",
        requiredMode = Schema.RequiredMode.REQUIRED,
        example = "ADPassword")
    private String password;
}
