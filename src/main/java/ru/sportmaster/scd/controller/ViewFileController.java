package ru.sportmaster.scd.controller;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;
import static ru.sportmaster.scd.config.OpenApiConfig.AUTH_NAME;
import static ru.sportmaster.scd.consts.OpenApiConst.FORM_UUID_HEADER_DESCRIPTION;
import static ru.sportmaster.scd.consts.OpenApiConst.SESSION_UUID_HEADER_DESCRIPTION;
import static ru.sportmaster.scd.consts.ParamNames.FORM_UUID_HEADER;
import static ru.sportmaster.scd.consts.ParamNames.SESSION_UUID_HEADER;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.sportmaster.scd.dto.view.ViewDownloadRequestDto;
import ru.sportmaster.scd.dto.view.ViewValidateFileResponseDto;
import ru.sportmaster.scd.logger.EndpointLog;
import ru.sportmaster.scd.service.view.ViewFileService;

@Tag(name = "View File")
@RestController
@RequestMapping("${scd.base-path}/view/file")
@RequiredArgsConstructor
@SecurityRequirement(name = AUTH_NAME)
public class ViewFileController {
    private final ViewFileService viewFileService;

    @EndpointLog
    @Operation(
        summary = "Выгрузка шаблона или данных view в формате excel",
        description = "Метод получает тип и параметры для выгрузки view в формате excel.",
        responses = @ApiResponse(
            responseCode = "200", description = "OK", content = @Content(schema = @Schema(
            description = "Скачиваеный файл в формате ts Blob",
            type = "file"
        )))
    )
    @PostMapping("/download")
    public Resource download(
        @Parameter(description = "Запрос на выгрузку файла excel", required = true)
        @RequestBody ViewDownloadRequestDto request,
        @Parameter(name = SESSION_UUID_HEADER, in = HEADER, description = SESSION_UUID_HEADER_DESCRIPTION)
        @RequestHeader(value = SESSION_UUID_HEADER, required = false) String sessionUuid,
        @Parameter(name = FORM_UUID_HEADER, in = HEADER, description = FORM_UUID_HEADER_DESCRIPTION)
        @RequestHeader(value = FORM_UUID_HEADER, required = false) String formUuid
    ) {
        return viewFileService.download(request, sessionUuid, formUuid);
    }

    @EndpointLog
    @Operation(
        summary = "Запуск валидации excel документа для обновления данных view.",
        description = "Метод получает для валидации файл с данными view с формате excel. "
            + "Возвращает uuid, для запроса статуса.",
        responses = @ApiResponse(
            responseCode = "200", description = "OK", content = @Content(schema = @Schema(
            description = "Идентификатор валидируемого файла"
        )))
    )
    @PutMapping(
        value = "/validate",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public String startValidate(
        @Parameter(description = "Название view")
        @RequestPart(value = "view") String view,
        @Parameter(description = "Файл данных в формате excel")
        @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        return viewFileService.startValidate(view, file);
    }

    @EndpointLog
    @Operation(
        summary = "Получение статуса валидации файла",
        description = "Получает статус валидации файла по полученому uuid. "
            + "Возвращает uuid, статус и прогресс обработки файла."
    )
    @GetMapping("/validate/{uuid}")
    public ViewValidateFileResponseDto getValidationStatus(
        @Parameter(description = "Идентификатор валидируемого файла", required = true)
        @PathVariable String uuid
    ) {
        return viewFileService.validateStatus(uuid);
    }

    @EndpointLog
    @Operation(
        summary = "Применение изменений для view, после валидации файла",
        description = "Применяет изменения view из файла после его обработки. Сохраняет данные в БД."
    )
    @PostMapping("/validate/save")
    public void saveFileData(
        @Parameter(description = "Идентификатор валидируемого файла", required = true)
        @RequestParam String uuid
    ) {
        viewFileService.saveFileData(uuid);
    }
}
