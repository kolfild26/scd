package ru.sportmaster.scd.controller;

import static ru.sportmaster.scd.config.OpenApiConfig.AUTH_NAME;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sportmaster.scd.algorithms.AlgComponentStatus;
import ru.sportmaster.scd.algorithms.information.AlgorithmInfo;
import ru.sportmaster.scd.dto.AlgorithmTasksDto;
import ru.sportmaster.scd.dto.PutAlgorithmsResponseDto;
import ru.sportmaster.scd.logger.EndpointLog;
import ru.sportmaster.scd.service.AlgorithmControlService;
import ru.sportmaster.scd.web.UserEnv;
import ru.sportmaster.scd.web.UserEnvironment;

@Slf4j
@Tag(name = "Algorithm")
@RestController
@RequestMapping("${scd.base-path}/algorithm")
@RequiredArgsConstructor
@SecurityRequirement(name = AUTH_NAME)
@ApiResponses(
    value = {
        @ApiResponse(
            responseCode = "400",
            description = "Ошибка запроса.",
            content = @Content(schema = @Schema)),
        @ApiResponse(
            responseCode = "403",
            description = "Ошибка авторизации.",
            content = @Content(schema = @Schema)),
        @ApiResponse(
            responseCode = "500",
            description = "Внутренняя ошибка",
            content = @Content(schema = @Schema))
    }
)
public class AlgorithmController {
    private final AlgorithmControlService algorithmControlService;

    @EndpointLog
    @Operation(
        description = "Постановка в очередь выполнения алгоритмов с указанными параметрами",
        summary = "Метод размещает алгоритмы в очереди выполнения. Возвращает идентификаторы размещенных алгоритмов.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "OK"),
            @ApiResponse(
                responseCode = "404",
                description = "Ошибка. Описание алгоритма не найдено.",
                content = @Content(schema = @Schema)),
            @ApiResponse(
                responseCode = "503",
                description = "Ошибка. Процесс выполнения алгоритма уже запущен.",
                content = @Content(schema = @Schema))
        }
    )
    @PostMapping("/put")
    public PutAlgorithmsResponseDto putAlgorithms(
        @Parameter(description = "Описание алгоритмов на выполнение", required = true)
        @RequestBody AlgorithmTasksDto request,
        @UserEnv UserEnvironment environment
    ) {
        return algorithmControlService.putAlgorithms(request, environment);
    }

    @EndpointLog
    @Operation(
        description = "Получение статуса выполнения алгоритма",
        summary = "Получение статуса выполнения алгоритма. Возвращает текущий статус запущенного алгоритма.",
        responses = @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(schema = @Schema(implementation = AlgComponentStatus.class)))
    )
    @GetMapping("/get-status")
    public AlgComponentStatus getAlgorithmStatus(
        @Parameter(description = "Идентификатор запущенного алгоритма", required = true)
        Long algorithmId
    ) {
        return algorithmControlService.getAlgorithmStatus(algorithmId);
    }

    @EndpointLog
    @Operation(
        description = "Получение статуса выполнения этапа алгоритма",
        summary = "Получение статуса выполнения этапа алгоритма. "
            + "Возвращает текущий статус этапа запущенного алгоритма.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "OK",
                content = @Content(schema = @Schema(implementation = AlgComponentStatus.class)))
        }
    )
    @GetMapping("/get-stage-status")
    public AlgComponentStatus getAlgStageStatus(
        @Parameter(description = "Идентификатор этапа запущенного алгоритма", required = true)
        Long algStageId
    ) {
        return algorithmControlService.getAlgStageStatus(algStageId);
    }

    @EndpointLog
    @Operation(
        description = "Получение статуса выполнения шага алгоритма",
        summary = "Получение статуса выполнения шага алгоритма. Возвращает текущий статус шага запущенного алгоритма.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "OK",
                content = @Content(schema = @Schema(implementation = AlgComponentStatus.class)))
        }
    )
    @GetMapping("/get-step-status")
    public AlgComponentStatus getAlgStepStatus(
        @Parameter(description = "Идентификатор шага запущенного алгоритма", required = true)
        Long algStepId
    ) {
        return algorithmControlService.getAlgStepStatus(algStepId);
    }

    @EndpointLog
    @Operation(
        description = "Получение информации по алгоритму",
        summary = "Получение информации по алгоритму. Возвращает полную информацию по алгоритму, включая этапы и шаги.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "OK",
                content = @Content(schema = @Schema(implementation = AlgorithmInfo.class))),
            @ApiResponse(
                responseCode = "404",
                description = "Ошибка. Алгоритм не найден.",
                content = @Content(schema = @Schema))
        }
    )
    @GetMapping("/get-info")
    public AlgorithmInfo getAlgorithmInfo(
        @Parameter(description = "Идентификатор алгоритма", required = true)
        Long algorithmId
    ) {
        return algorithmControlService.getAlgorithmInfo(algorithmId);
    }

    @EndpointLog
    @Operation(
        description = "Сервисный запуск обработки очереди выполнения алгоритмов",
        summary = "Сервисный запуск обработки очереди выполнения алгоритмов.",
        responses = @ApiResponse(
            responseCode = "200",
            description = "OK")
    )
    @GetMapping("/service-start-execute")
    public Boolean serviceStartExecuteAlgorithms() {
        return algorithmControlService.serviceStartExecuteAlgorithms();
    }
}
