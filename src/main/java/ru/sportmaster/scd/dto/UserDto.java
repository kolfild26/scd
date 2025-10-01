package ru.sportmaster.scd.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto implements Serializable {
    @Schema(
        description = "Идентификатор пользователя",
        name = "id",
        requiredMode = Schema.RequiredMode.REQUIRED,
        example = "1")
    private Long id;
    @Schema(
        description = "Имя пользователя",
        name = "login",
        requiredMode = Schema.RequiredMode.REQUIRED,
        example = "ADUsername")
    private String login;
    @Schema(
        description = "Имя",
        name = "firstName",
        example = "Иван")
    private String firstName;
    @Schema(
        description = "Фамилия",
        name = "lastName",
        example = "Иванов")
    private String lastName;
}
