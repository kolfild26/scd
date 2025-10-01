package ru.sportmaster.scd.controller;

import static ru.sportmaster.scd.config.OpenApiConfig.AUTH_NAME;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.sportmaster.scd.dto.adjustment.AdjustmentAddSpecificationRequestDto;
import ru.sportmaster.scd.dto.adjustment.AdjustmentDownloadRequestDto;
import ru.sportmaster.scd.dto.adjustment.AdjustmentValidationRequestDto;
import ru.sportmaster.scd.dto.view.ViewValidateFileResponseDto;
import ru.sportmaster.scd.logger.EndpointLog;
import ru.sportmaster.scd.service.adjustment.AdjustmentService;
import ru.sportmaster.scd.ui.view.type.UiView;
import ru.sportmaster.scd.web.UserEnv;
import ru.sportmaster.scd.web.UserEnvironment;

@Slf4j
@Tag(name = "Adjustment")
@RestController
@RequestMapping("${scd.base-path}/adjustment")
@RequiredArgsConstructor
@SecurityRequirement(name = AUTH_NAME)
public class AdjustmentController {
    private final AdjustmentService adjustmentService;

    @EndpointLog
    @Operation(
        summary = "Получение настройки отображения строк для документа корректировки",
        description = "Метод получает настройки отображения строк для указанного документа корректировки."
    )
    @GetMapping("/view")
    public UiView getDocumentRowView(
        @Parameter(description = "Идентификатор документа корректировки")
        @RequestParam Long documentId
    ) {
        return adjustmentService.getDocumentRowView(documentId);
    }

    @EndpointLog
    @Operation(
        summary = "Валидация документа корректировки",
        description = "Метод получает для валидации документ корректировки в формате excel. "
            + "Возвращает uuid для получения статуса обработки документа."
    )
    @PutMapping(
        value = "/validate",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public String validate(
        @Parameter(description = "Запрос на валидацию документа корректировки")
        @RequestPart(value = "request") AdjustmentValidationRequestDto request,
        @Parameter(description = "Документ корректировки в формате excel")
        @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        return adjustmentService.validate(request, file);
    }

    @EndpointLog
    @Operation(
        summary = "Получение статуса валидации документа корректировки",
        description = "Получает статус валидации документа корректировки по полученому uuid. "
            + "Возвращает uuid, статус и прогресс обработки документа."
    )
    @GetMapping("/validate/{uuid}")
    public ViewValidateFileResponseDto getValidationStatus(
        @Parameter(description = "Документ корректировки в формате excel", required = true)
        @PathVariable String uuid
    ) {
        return adjustmentService.getValidationStatus(uuid);
    }

    @EndpointLog
    @Operation(
        summary = "Скачивание спецификации шаблона или документа",
        description = "Метод получает идетификатор документы или тип шаблона для скачивания."
    )
    @PostMapping("/download")
    public Resource download(
        @Parameter(description = "Идентификатор документа корректировки", required = true)
        @RequestBody AdjustmentDownloadRequestDto request
    ) {
        return adjustmentService.download(request);
    }

    @EndpointLog
    @Operation(
        summary = "Утверждение документа корректировки",
        description = "Метод получает идентификатор документа для его утверждения."
    )
    @PostMapping("/approve/{documentId}")
    public void approve(
        @Parameter(description = "Идентификатор документа корректировки", required = true)
        @PathVariable Long documentId,
        @Parameter(hidden = true)
        @UserEnv UserEnvironment environment
    ) {
        adjustmentService.approve(documentId, environment);
    }

    @EndpointLog
    @Operation(
        summary = "Отмена документа корректировки",
        description = "Метод получает идентификатор документа для его отмены."
    )
    @PostMapping("/cancel/{documentId}")
    public void cancel(
        @Parameter(description = "Идентификатор документа корректировки", required = true)
        @PathVariable Long documentId,
        @Parameter(hidden = true)
        @UserEnv UserEnvironment environment
    ) {
        adjustmentService.cancel(documentId, environment);
    }

    @EndpointLog
    @Operation(
        summary = "Удаление документа корректировки",
        description = "Метод получает идентификатор документа для его удаления."
    )
    @DeleteMapping("/delete/{documentId}")
    public void delete(
        @Parameter(description = "Идентификатор документа корректировки", required = true)
        @PathVariable Long documentId,
        @Parameter(hidden = true)
        @UserEnv UserEnvironment environment
    ) {
        adjustmentService.delete(documentId, environment);
    }

    @EndpointLog
    @Operation(
        summary = "Добавление спецификации корректировки",
        description = "Метод создает спецификацию корректировки. Возвращает идентификатор созданной спецификации"
    )
    @PostMapping("/add")
    public Long addSpecification(
        @Parameter(description = "Запрос на создание спецификации корректировки")
        @RequestBody AdjustmentAddSpecificationRequestDto request,
        @Parameter(hidden = true)
        @UserEnv UserEnvironment environment
    ) {
        return adjustmentService.addSpecification(request, environment);
    }
}
