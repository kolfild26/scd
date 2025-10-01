package ru.sportmaster.scd.controller;

import static ru.sportmaster.scd.config.OpenApiConfig.AUTH_NAME;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.sportmaster.scd.dto.adjustment.editor.AlgEditorSchemaDto;
import ru.sportmaster.scd.dto.adjustment.editor.CellDataDto;
import ru.sportmaster.scd.dto.adjustment.editor.CellType;
import ru.sportmaster.scd.logger.EndpointLog;
import ru.sportmaster.scd.service.editor.AlgEditorService;
import ru.sportmaster.scd.web.UserEnv;
import ru.sportmaster.scd.web.UserEnvironment;

@Slf4j
@Tag(name = "AlgEditor")
@RestController
@RequestMapping("${scd.base-path}/alg-editor")
@RequiredArgsConstructor
@SecurityRequirement(name = AUTH_NAME)
public class AlgEditorController {
    private final AlgEditorService algEditorService;

    @EndpointLog
    @Operation(
        description = "Получение списка схем описаний алгоритмов",
        summary = "Метод получения списка схем алгоритмов"
    )
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema))
    @GetMapping("/schemes")
    public List<AlgEditorSchemaDto> getSchemes() {
        return algEditorService.getSchemes();
    }

    @EndpointLog
    @Operation(
        description = "Получение списка сущностей для ячеек",
        summary = "Метод получения списка сущностей для ячеек по типу ячейки"
    )
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema))
    @GetMapping("/cell-data")
    public List<CellDataDto> getCellData(@RequestParam CellType type) {
        return algEditorService.getCellData(type);
    }

    @EndpointLog
    @Operation(
        description = "Сохранение изменений схемы описания алгоритма",
        summary = "Метод сохранения схемы описания алгоритма"
    )
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema))
    @PostMapping("/save")
    public AlgEditorSchemaDto saveSchema(@RequestBody AlgEditorSchemaDto schema,
                                         @UserEnv UserEnvironment environment) {
        return algEditorService.saveSchema(schema, environment);
    }

    @EndpointLog
    @Operation(
        description = "Удаление схемы описания алгоритма",
        summary = "Метод удаления описания алгоритма"
    )
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema))
    @DeleteMapping("/{id}")
    public void deleteSchema(@PathVariable Long id) {
        algEditorService.deleteSchema(id);
    }
}
