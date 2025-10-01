package ru.sportmaster.scd.controller;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.sportmaster.scd.dto.view.UiViewSearchRequestDto;
import ru.sportmaster.scd.dto.view.UiViewSearchResponseDto;
import ru.sportmaster.scd.logger.EndpointLog;
import ru.sportmaster.scd.mementity.IMemEntityResolver;
import ru.sportmaster.scd.ui.view.IUiViewSearchProcessor;
import ru.sportmaster.scd.ui.view.UiViewManager;
import ru.sportmaster.scd.ui.view.UiViewResolver;
import ru.sportmaster.scd.web.UserEnv;
import ru.sportmaster.scd.web.UserEnvironment;

@Slf4j
@Tag(name = "Search")
@RestController
@RequestMapping("${scd.base-path}/search")
@RequiredArgsConstructor
@SecurityRequirement(name = AUTH_NAME)
public class SearchController {
    private final UiViewManager viewManager;

    @EndpointLog
    @Operation(
        summary = "Запуск постраничного поиска",
        description = "Метод запускает постраничный поиск. Возвращает данные поиска постранично. "
            + "Необходимо выполнять для каждой страницы view"
    )
    @PostMapping("/start")
    public UiViewSearchResponseDto startSearch(
        @Parameter(name = SESSION_UUID_HEADER, in = HEADER, description = SESSION_UUID_HEADER_DESCRIPTION)
        @RequestHeader(value = SESSION_UUID_HEADER, required = false) String sessionUuid,
        @Parameter(name = FORM_UUID_HEADER, in = HEADER, description = FORM_UUID_HEADER_DESCRIPTION)
        @RequestHeader(value = FORM_UUID_HEADER, required = false) String formUuid,
        @Parameter(description = "Запрос поиска")
        @RequestBody UiViewSearchRequestDto request,
        @Parameter(hidden = true)
        @UserEnv UserEnvironment environment) {
        UiViewResolver resolver = viewManager.getResolver(request.getView());
        if (resolver instanceof IMemEntityResolver memEntityResolver) {
            request.setSessionUuid(sessionUuid);
            request.setFormUuid(formUuid);
            return memEntityResolver.pageSearch(request);
        }
        if (resolver instanceof IUiViewSearchProcessor searchProcessor) {
            return searchProcessor.pageSearch(request, environment.getUserId());
        }
        return null;
    }

    @EndpointLog
    @Operation(
        summary = "Получение значений поля для view object.",
        description = "Метод получает значения поля для view object. Возвращает список значений поля."
    )
    @GetMapping("/get")
    public int getSearchItemIndex(
        @Parameter(name = SESSION_UUID_HEADER, in = HEADER, description = SESSION_UUID_HEADER_DESCRIPTION)
        @RequestHeader(value = SESSION_UUID_HEADER, required = false) String sessionUuid,
        @Parameter(description = "Текущее view")
        @RequestParam("view") String view,
        @Parameter(description = "Текущая позиция поиска")
        @RequestParam("current") Integer currentPosition,
        @Parameter(description = "Направление поиска")
        @RequestParam("direction") Byte direction,
        @Parameter(hidden = true)
        @UserEnv UserEnvironment environment) {
        UiViewResolver resolver = viewManager.getResolver(view);
        if (resolver instanceof IMemEntityResolver memEntityResolver) {
            return memEntityResolver.getSearchItemPosition(sessionUuid, currentPosition, direction);
        }
        if (resolver instanceof IUiViewSearchProcessor searchProcessor) {
            return searchProcessor.getSearchItemPosition(view, environment.getUserId(), currentPosition, direction);
        }
        return -1;
    }

    @DeleteMapping("/clear")
    public void clearSearch(
        @Parameter(name = SESSION_UUID_HEADER, in = HEADER, description = SESSION_UUID_HEADER_DESCRIPTION)
        @RequestHeader(value = SESSION_UUID_HEADER, required = false) String sessionUuid,
        @Parameter(description = "Текущее view")
        @RequestParam("view") String view,
        @Parameter(hidden = true)
        @UserEnv UserEnvironment environment) {
        UiViewResolver resolver = viewManager.getResolver(view);
        if (resolver instanceof IMemEntityResolver memEntityResolver) {
            memEntityResolver.clearSearch(sessionUuid);
        }
        if (resolver instanceof IUiViewSearchProcessor searchProcessor) {
            searchProcessor.clearSearch(view, environment.getUserId());
        }
    }
}
