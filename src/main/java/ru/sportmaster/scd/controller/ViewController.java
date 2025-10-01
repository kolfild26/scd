package ru.sportmaster.scd.controller;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;
import static ru.sportmaster.scd.config.OpenApiConfig.AUTH_NAME;
import static ru.sportmaster.scd.consts.OpenApiConst.FORM_UUID_HEADER_DESCRIPTION;
import static ru.sportmaster.scd.consts.OpenApiConst.SESSION_UUID_HEADER_DESCRIPTION;
import static ru.sportmaster.scd.consts.OpenApiConst.UI_VIEW_REF;
import static ru.sportmaster.scd.consts.ParamNames.FORM_UUID_HEADER;
import static ru.sportmaster.scd.consts.ParamNames.SESSION_UUID_HEADER;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.sportmaster.scd.logger.EndpointLog;
import ru.sportmaster.scd.service.view.ViewObjectService;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryItem;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryType;
import ru.sportmaster.scd.ui.view.type.UIViewFetchFieldRequest;
import ru.sportmaster.scd.ui.view.type.UIViewFetchFieldValuesRequest;
import ru.sportmaster.scd.ui.view.type.UiView;
import ru.sportmaster.scd.ui.view.type.UiViewCrudRequest;
import ru.sportmaster.scd.ui.view.type.UiViewFetchRequest;
import ru.sportmaster.scd.web.UserEnv;
import ru.sportmaster.scd.web.UserEnvironment;

@Tag(name = "View Object")
@RestController
@RequestMapping("${scd.base-path}/view")
@RequiredArgsConstructor
@SecurityRequirement(name = AUTH_NAME)
public class ViewController {
    private final ViewObjectService viewObjectService;

    @EndpointLog
    @Operation(
        summary = "Получение списка доступных view object.",
        description = "Метод получает список view object. "
            + "Возвращает указанные объекты с доступными полями (аттрибутами).",
        responses = @ApiResponse(
            responseCode = "200", description = "OK", content = @Content(schema = @Schema, schemaProperties = {
                @SchemaProperty(name = "AdjustmentDoc", schema = @Schema(implementation = UiView.class))
            }))
    )
    @GetMapping
    public Map<String, UiView> getViews(
        @Parameter(description = "Список названий view object", required = true, schema = @Schema(ref = UI_VIEW_REF))
        @RequestParam("names") List<String> viewNames
    ) {
        return viewObjectService.getViews(viewNames);
    }

    @EndpointLog
    @Operation(
        summary = "Получение значений словарей.",
        description = "Метод получает словарей. Возвращает указанные словари со значениями.",
        responses = @ApiResponse(
            responseCode = "200", description = "OK", content = @Content(schema = @Schema, schemaProperties = {
                @SchemaProperty(name = "BUSINESS", schema = @Schema(
                    example = "[{\"id\":120890299,\"label\":\"Спорт\",\"value\":120890299},"
                        + "{\"id\":120880299,\"label\":\"OSTIN\",\"value\":120880299},"
                        + "{\"id\":120870299,\"label\":\"FUNDAY\",\"value\":120870299}]"
                    ))
                }))
    )
    @GetMapping("/dictionary")
    public Map<DictionaryType, List<DictionaryItem>> getDictionaries(
        @Parameter(description = "Enum типов словарей", required = true)
        @RequestParam List<DictionaryType> types
    ) {
        return viewObjectService.getDictionaries(types);
    }

    @EndpointLog
    @Operation(
        summary = "Получение данных для view object.",
        description = "Метод получает данные для view object. Возвращает список json c оберткой пагинации."
    )
    @PostMapping("/fetch")
    public Page<ObjectNode> findAll(
        @Parameter(required = true)
        @RequestBody UiViewFetchRequest request,
        @Parameter(name = SESSION_UUID_HEADER, in = HEADER, description = SESSION_UUID_HEADER_DESCRIPTION)
        @RequestHeader(value = SESSION_UUID_HEADER, required = false) String sessionUuid,
        @Parameter(name = FORM_UUID_HEADER, in = HEADER, description = FORM_UUID_HEADER_DESCRIPTION)
        @RequestHeader(value = FORM_UUID_HEADER, required = false) String formUuid
    ) {
        return viewObjectService.findAll(request, sessionUuid, formUuid);
    }

    @EndpointLog
    @Operation(
        summary = "Получение значений колонок для view object.",
        description = "Метод получает значения колонок для view object. Возвращает список json c оберткой пагинации."
    )
    @PostMapping("/fetch-only-fields")
    public Page<ObjectNode> findAllOnlyFields(
        @Parameter(required = true)
        @RequestBody UIViewFetchFieldRequest request
    ) {
        return viewObjectService.findAllOnlyFields(request);
    }

    @EndpointLog
    @Operation(
        summary = "Получение значений поля для view object.",
        description = "Метод получает значения поля для view object. Возвращает список значений поля.",
        responses = @ApiResponse(
            responseCode = "200", description = "OK", content = @Content(schema = @Schema(
                example = "[\"Черновик\",\"Утверждено\"]"
            )))
    )
    @PostMapping("/values")
    public List<?> findFieldValues(
        @Parameter(required = true)
        @RequestBody UIViewFetchFieldValuesRequest request,
        @Parameter(name = SESSION_UUID_HEADER, in = HEADER, description = SESSION_UUID_HEADER_DESCRIPTION)
        @RequestHeader(value = SESSION_UUID_HEADER, required = false) String sessionUuid,
        @Parameter(name = FORM_UUID_HEADER, in = HEADER, description = FORM_UUID_HEADER_DESCRIPTION)
        @RequestHeader(value = FORM_UUID_HEADER, required = false) String formUuid
    ) {
        return viewObjectService.findFieldValues(request, sessionUuid, formUuid);
    }

    @EndpointLog
    @Operation(
        summary = "CRUD для данных view object.",
        description = "CRUD операции для view object.",
        responses = @ApiResponse(
            responseCode = "200", description = "OK", content = @Content(schema = @Schema(
            type = "list",
            example = "[{\"id\":120890299,\"label\":\"Спорт\",\"value\":120890299},"
                + "{\"id\":120880299,\"label\":\"OSTIN\",\"value\":120880299},"
                + "{\"id\":120870299,\"label\":\"FUNDAY\",\"value\":120870299}]"
        )))
    )
    @PostMapping("/change")
    public List<ObjectNode> change(
        @Parameter(required = true)
        @RequestBody List<UiViewCrudRequest> requests,
        @Parameter(hidden = true)
        @UserEnv UserEnvironment environment
    ) {
        return viewObjectService.change(requests, environment);
    }

    @EndpointLog
    @Operation(
        summary = "Очистка временных данных для view object.",
        description = "Метод очищает временные данные кеша для view object.."
    )
    @DeleteMapping("/clear")
    public void clear(
        @Parameter(description = "Текущее view", required = true)
        @RequestParam("view") String view,
        @Parameter(name = FORM_UUID_HEADER, in = HEADER, description = FORM_UUID_HEADER_DESCRIPTION)
        @RequestHeader(value = FORM_UUID_HEADER, required = false) String formUuid
    ) {
        viewObjectService.clear(view, formUuid);
    }
}
