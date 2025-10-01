package ru.sportmaster.scd.controller;

import static ru.sportmaster.scd.config.OpenApiConfig.AUTH_NAME;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.sportmaster.scd.dto.pivot.PivotConfigDto;
import ru.sportmaster.scd.dto.pivot.PivotConfigParametersDto;
import ru.sportmaster.scd.dto.pivot.PivotConfigStatusDto;
import ru.sportmaster.scd.dto.pivot.PivotDataRequest;
import ru.sportmaster.scd.dto.pivot.PivotDownloadRequestDto;
import ru.sportmaster.scd.dto.pivot.PivotFilterDto;
import ru.sportmaster.scd.dto.pivot.PivotPreparingFileDto;
import ru.sportmaster.scd.dto.pivot.PivotSearchDataRequestDto;
import ru.sportmaster.scd.dto.pivot.PivotValidationDataRequestDto;
import ru.sportmaster.scd.logger.EndpointLog;
import ru.sportmaster.scd.service.PivotService;
import ru.sportmaster.scd.web.UserEnv;
import ru.sportmaster.scd.web.UserEnvironment;

@Slf4j
@Tag(name = "Pivot")
@RestController
@RequestMapping("${scd.base-path}/pivot")
@RequiredArgsConstructor
@SecurityRequirement(name = AUTH_NAME)
public class PivotController {
    private final PivotService pivotService;

    @EndpointLog
    @Operation(
        summary = "Получение конфигурации для сводной таблицы",
        description = "Метод получает конфигурацию для сводной таблицы. По идентификатору и выбранному "
            + "отображению сводной."
    )
    @GetMapping("/config")
    public PivotConfigDto getPivotConfig(
        @Parameter(description = "Идентификатор сводной таблицы", example = "MATRIX_PIVOT_BASE_LF", required = true)
        @RequestParam String pivotId,
        @Parameter(hidden = true)
        @UserEnv UserEnvironment environment
    ) {
        return pivotService.getPivotConfig(pivotId, environment);
    }

    @EndpointLog
    @Operation(
        summary = "Сохранение/Обновление конфигурации сводной таблицы",
        description = "Метод сохраняет/обновляет конфигурацию сводной таблицы."
    )
    @PutMapping("/config")
    public void savePivotConfig(
        @Parameter(description = "Идентификатор сводной таблицы", example = "MATRIX_PIVOT_BASE_LF", required = true)
        @RequestParam String pivotId,
        @Parameter(description = "Объект конфигурации сводной для сохранения", required = true)
        @RequestBody PivotConfigParametersDto parameters,
        @Parameter(hidden = true)
        @UserEnv UserEnvironment environment
    ) {
        pivotService.savePivotConfig(pivotId, parameters, environment);
    }

    @EndpointLog
    @Operation(
        summary = "Получение статуса сохранения/обновление конфигурации сводной таблицы",
        description = "Метод проверяет статус сохранения/обновления конфигурации сводной таблицы."
    )
    @GetMapping("/config-status")
    public PivotConfigStatusDto savePivotConfigStatus(
        @Parameter(description = "Идентификатор сводной таблицы", example = "MATRIX_PIVOT_BASE_LF", required = true)
        @RequestParam String pivotId,
        @Parameter(hidden = true)
        @UserEnv UserEnvironment environment
    ) {
        return pivotService.getSavePivotConfigStatus(pivotId, environment);
    }

    @EndpointLog
    @Operation(
        summary = "Получение списка сохраненных фильтр сетов для сводной",
        description = "Метод получения списка сохраненных фильтр сетов по идентификатору сводной."
    )
    @GetMapping("/filter-set")
    public List<PivotFilterDto> getFilterSets(
        @Parameter(description = "Идентификатор сводной", required = true)
        @RequestParam String pivotId,
        @Parameter(hidden = true)
        @UserEnv UserEnvironment environment
    ) {
        return pivotService.getFilterSets(pivotId, environment);
    }

    @EndpointLog
    @Operation(
        summary = "Сохранения фильтр сетов для сводной",
        description = "Метод сохраняет фильтр сет для сводной."
    )
    @PostMapping("/filter-set")
    public void saveFilterSet(
        @Parameter(description = "Идентификатор сводной", required = true)
        @RequestParam String pivotId,
        @Parameter(description = "Запрос на сохранение фильтр сета", required = true)
        @RequestBody PivotFilterDto request,
        @Parameter(hidden = true)
        @UserEnv UserEnvironment environment
    ) {
        pivotService.saveFilterSet(pivotId, request, environment);
    }

    @EndpointLog
    @Operation(
        summary = "Удаление сохраненных фильтр сетов.",
        description = "Метод удаления сохраненных фильтр сетов по идентификатору сводной и названиям фильтр сетов."
    )
    @DeleteMapping("/filter-set")
    public void deleteFilterSets(
        @Parameter(description = "Идентификатор сводной", required = true)
        @RequestParam String pivotId,
        @Parameter(description = "Названия фильтр-сетов для удаления", required = true)
        @RequestParam List<String> names,
        @Parameter(hidden = true)
        @UserEnv UserEnvironment environment
    ) {
        pivotService.deleteFilterSets(pivotId, names, environment);
    }

    @EndpointLog
    @Operation(
        summary = "Получение данных для сводной.",
        description = "Метод получение данных для сводной. Возвращает список строк сводной."
    )
    @PostMapping("/data")
    public List<Object[]> getPivotData(
        @Parameter(description = "Запрос для получения данных сводной", required = true)
        @RequestBody PivotDataRequest request,
        @Parameter(hidden = true)
        @UserEnv UserEnvironment environment
    ) {
        return pivotService.getPivotData(request, environment);
    }

    @EndpointLog
    @Operation(
        summary = "Получение данных для сводной в виде объектов.",
        description = "Метод получение данных для сводной. Возвращает список строк сводной в виде строк."
    )
    @PostMapping("/data-string")
    public List<String> getPivotDataString(
        @Parameter(description = "Запрос для получения данных сводной в виде строк", required = true)
        @RequestBody PivotDataRequest request,
        @Parameter(hidden = true)
        @UserEnv UserEnvironment environment
    ) {
        return pivotService.getPivotDataString(request, environment);
    }

    @EndpointLog
    @Operation(
        summary = "Получение данных для сводной в виде объектов.",
        description = "Метод получение данных для сводной. Возвращает список строк сводной в виде объектов."
    )
    @PostMapping("/data-json")
    public List<JsonNode> getPivotDataJson(
        @Parameter(description = "Запрос для получения данных сводной в виде объектов", required = true)
        @RequestBody PivotDataRequest request,
        @Parameter(hidden = true)
        @UserEnv UserEnvironment environment
    ) {
        return pivotService.getPivotDataJson(request, environment);
    }

    @EndpointLog
    @Operation(
        summary = "Получения номеров строк для поиска.",
        description = "Метод получения номеров строк для поиска. Возвращает список номеров строк сводной."
    )
    @PostMapping("/search")
    public List<Long> searchPivotRows(
        @Parameter(description = "Запрос для получения номеров строк для поиска", required = true)
        @RequestBody PivotSearchDataRequestDto request,
        @Parameter(hidden = true)
        @UserEnv UserEnvironment environment
    ) {
        return pivotService.searchPivotRows(request, environment);
    }

    @EndpointLog
    @Operation(
        summary = "Получение номеров строк с ошибками.",
        description = "Метод валидации строк сводной таблицы. Получает тип ошибки для проведения валидации. "
            + "Возвращает список номеров строк сводной."
    )
    @PostMapping("/validate")
    public List<Long> validatePivotRows(
        @Parameter(description = "Запрос для получения номеров строк по итогам валидации", required = true)
        @RequestBody PivotValidationDataRequestDto request,
        @Parameter(hidden = true)
        @UserEnv UserEnvironment environment
    ) {
        return pivotService.validatePivotRows(request, environment);
    }

    @EndpointLog
    @Operation(
        summary = "Подготовка файла сводной таблицы для скачивания.",
        description = "Подготовка файла таблицы для скачивания. Возвращает уникальный идентификатор job подготовки."
    )
    @PostMapping("/download/prepare")
    public String startPivotDownloadPrepare(
        @Parameter(description = "Запрос на формирования файла", required = true)
        @RequestBody PivotDownloadRequestDto request,
        @Parameter(hidden = true)
        @UserEnv UserEnvironment environment
    ) {
        return pivotService.startPivotPreparingForDownload(request, environment);
    }

    @EndpointLog
    @Operation(
        summary = "Проверка статуса подготовки файла для скачивания.",
        description = "Проверка статуса подготовки файла для скачивания. Возвращает статус файла таблицы."
    )
    @GetMapping("/download/prepare-status")
    public PivotPreparingFileDto checkPivotDownloadPrepareStatus(
        @Parameter(description = "Идентификатор файла", required = true)
        @RequestParam String uuid
    ) {
        return pivotService.checkPreparePivotStatus(uuid);
    }

    @EndpointLog
    @Operation(
        summary = "Скачивание сводной таблице в указанном формате.",
        description = "Скачивание сводной таблицы. Возвращает данные сводной таблицей."
    )
    @GetMapping("/download/file")
    public ByteArrayResource downloadPivotFile(
        @Parameter(description = "Идентификатор файла", required = true)
        @RequestParam String uuid
    ) {
        return new ByteArrayResource(pivotService.downloadPivotFile(uuid));
    }

    @GetMapping("/field-values")
    public List<Object> getUniqFieldValues(
        @Parameter(description = "Идентификатор сводной таблицы", example = "MATRIX_PIVOT_BASE_LF", required = true)
        @RequestParam String pivotId,
        @Parameter(description = "Поле сводной таблицы", required = true)
        @RequestParam String field,
        @Parameter(hidden = true)
        @UserEnv UserEnvironment environment
    ) {
        return pivotService.getUniqFieldValues(pivotId, field, environment);
    }
}
