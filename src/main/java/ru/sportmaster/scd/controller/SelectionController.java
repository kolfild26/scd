package ru.sportmaster.scd.controller;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;
import static java.util.Optional.ofNullable;
import static ru.sportmaster.scd.config.OpenApiConfig.AUTH_NAME;
import static ru.sportmaster.scd.consts.OpenApiConst.FORM_UUID_HEADER_DESCRIPTION;
import static ru.sportmaster.scd.consts.OpenApiConst.SESSION_UUID_HEADER_DESCRIPTION;
import static ru.sportmaster.scd.consts.ParamNames.FORM_UUID_HEADER;
import static ru.sportmaster.scd.consts.ParamNames.SESSION_UUID_HEADER;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.sportmaster.scd.consts.ParamNames;
import ru.sportmaster.scd.dto.mementity.MemEntitySelectDto;
import ru.sportmaster.scd.dto.view.SelectionRequestDto;
import ru.sportmaster.scd.logger.EndpointLog;
import ru.sportmaster.scd.mementity.IMemEntityResolver;
import ru.sportmaster.scd.ui.view.UiViewManager;
import ru.sportmaster.scd.web.UserEnv;
import ru.sportmaster.scd.web.UserEnvironment;

@Slf4j
@Tag(name = "Selection")
@RestController
@RequestMapping("${scd.base-path}/select")
@RequiredArgsConstructor
@SecurityRequirement(name = AUTH_NAME)
public class SelectionController {
    private final UiViewManager viewManager;

    @EndpointLog
    @Operation(
        summary = "Запрос получения статуса выбора",
        description = "Метод получает статус выбора для view object."
    )
    @GetMapping("/get")
    public MemEntitySelectDto<?, ?> get(
        @Parameter(description = "Текущий view object")
        @RequestParam String view,
        @Parameter(name = SESSION_UUID_HEADER, in = HEADER, description = SESSION_UUID_HEADER_DESCRIPTION)
        @RequestHeader(SESSION_UUID_HEADER) String sessionUuid,
        @Parameter(name = FORM_UUID_HEADER, in = HEADER, description = FORM_UUID_HEADER_DESCRIPTION)
        @RequestHeader(FORM_UUID_HEADER) String formUuid) {
        if (viewManager.getResolver(view) instanceof IMemEntityResolver memEntityResolver) {
            return memEntityResolver.getSelectionInfo(sessionUuid, formUuid);
        }
        return null;
    }

    @EndpointLog
    @Operation(
        summary = "Запрос на изменения серверного выбора",
        description = "Метод получает запрос выбора и сохраняет состояние выбора в api."
    )
    @PostMapping("/change")
    public void select(
        @RequestBody SelectionRequestDto request,
        @Parameter(name = SESSION_UUID_HEADER, in = HEADER, description = SESSION_UUID_HEADER_DESCRIPTION)
        @RequestHeader(SESSION_UUID_HEADER) String sessionUuid,
        @Parameter(name = FORM_UUID_HEADER, in = HEADER, description = FORM_UUID_HEADER_DESCRIPTION)
        @RequestHeader(FORM_UUID_HEADER) String formUuid,
        @Parameter(hidden = true)
        @UserEnv UserEnvironment environment) {
        if (viewManager.getResolver(request.getView()) instanceof IMemEntityResolver memEntityResolver) {
            MDC.put(
                ParamNames.USER_ID_FIELD,
                ofNullable(environment.getUserId()).map(Object::toString).orElse(null)
            );
            memEntityResolver.exec(sessionUuid, formUuid, request);
        }
    }
}
