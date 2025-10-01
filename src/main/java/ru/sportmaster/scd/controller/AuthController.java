package ru.sportmaster.scd.controller;

import static ru.sportmaster.scd.config.OpenApiConfig.AUTH_NAME;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sportmaster.scd.dto.InfoDto;
import ru.sportmaster.scd.dto.LoginRequestDto;
import ru.sportmaster.scd.dto.UserDto;
import ru.sportmaster.scd.logger.EndpointLog;
import ru.sportmaster.scd.service.AuthService;
import ru.sportmaster.scd.service.EnvService;

@Slf4j
@Tag(name = "Auth")
@RestController
@RequestMapping("${scd.base-path}/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final EnvService envService;

    @EndpointLog
    @Operation(
        description = "Авторизация пользователя по username и password",
        summary = "Метод авторизации пользователя",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "OK",
                content = @Content(
                    examples = {
                        @ExampleObject(
                            name = "Токен авторизации",
                            summary = "Response",
                            value = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0MSIsImV4cCI6MTY4MTgxMTEzNX0.u3hTVtuOgJs"
                                + "5T41_hRTRYvw3Gjqz_jPZPJ7Bd4vJSCCcEhwsbcXXXoKuEflIVU0NCm4tV3tKS3g5SeDspH2f6A")}))})
    @PostMapping("/login")
    public String authUser(
        @Parameter(description = "Параметры авторизации", required = true)
        @RequestBody LoginRequestDto request
    ) {
        return authService.auth(request);
    }

    @EndpointLog
    @Operation(
        description = "Получение данных текущего пользователя",
        summary = "Метод получения пользователя"
    )
    @ApiResponse(responseCode = "200", description = "OK",
        content = @Content(schema = @Schema(implementation = UserDto.class)))
    @SecurityRequirement(name = AUTH_NAME)
    @GetMapping("/current")
    public UserDto getCurrentUser() {
        return authService.getCurrentUser();
    }

    @EndpointLog
    @Operation(
        description = "Получение данных сервиса",
        summary = "Метод получения версии и окружения сервиса"
    )
    @GetMapping("/info")
    public InfoDto getInfo() {
        return envService.getInfo();
    }
}
