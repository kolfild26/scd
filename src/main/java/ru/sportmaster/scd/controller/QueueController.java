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
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sportmaster.scd.dto.AlgorithmQueueDto;
import ru.sportmaster.scd.logger.EndpointLog;
import ru.sportmaster.scd.service.task.QueueService;

@Slf4j
@Tag(name = "Queue")
@RestController
@RequestMapping("${scd.base-path}/queue")
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
public class QueueController {
    private final QueueService queueService;

    @EndpointLog
    @Operation(
        description = "Получение информации по состоянию очереди выполнения алгоритмов",
        summary = "Получение информации по состоянию очереди выполнения алгоритмов. "
            + "Возвращает текущее содержание очереди выполнения алгоритмов.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "OK")
        }
    )
    @GetMapping("/get-state")
    public List<AlgorithmQueueDto> getQueueState() {
        return queueService.getQueueState();
    }

    @EndpointLog
    @Operation(
        description = "Отмена выполнения алгоритма из очереди выполнения алгоритмов",
        summary = "Отмена выполнения алгоритма из очереди выполнения алгоритмов. "
            + "Возвращается количество отмененных алгоритмов.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "OK")
        }
    )
    @GetMapping("/cancel-algorithm")
    public int cancelAlgorithm(
        @Parameter(description = "Идентификатор отменяемого алгоритма", required = true)
        Long algorithmId
    ) {
        return queueService.cancelAlgorithm(algorithmId);
    }

    @EndpointLog
    @Operation(
        description = "Отмена выполнения группы алгоритмов из очереди выполнения алгоритмов",
        summary = "Отмена выполнения группы алгоритмов из очереди выполнения алгоритмов. "
            + "Возвращается количество отмененных алгоритмов.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "OK")
        }
    )
    @GetMapping("/cancel-group")
    public int cancelGroup(
        @Parameter(description = "Идентификатор группы отменяемых алгоритмов", required = true)
        Long algorithmGroup
    ) {
        return queueService.cancelGroup(algorithmGroup);
    }
}
