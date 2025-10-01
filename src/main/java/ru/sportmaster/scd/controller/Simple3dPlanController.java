package ru.sportmaster.scd.controller;

import static ru.sportmaster.scd.config.OpenApiConfig.AUTH_NAME;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.sportmaster.scd.logger.EndpointLog;
import ru.sportmaster.scd.service.Simple3dPlanService;

@Slf4j
@Tag(name = "Simple3dPlan")
@RestController
@RequestMapping("${scd.base-path}/simple-3d-plan")
@RequiredArgsConstructor
@SecurityRequirement(name = AUTH_NAME)
public class Simple3dPlanController {
    private final Simple3dPlanService simple3dPlanService;

    @EndpointLog
    @Operation(
        summary = "Копирование данных ценовых уровней",
        description = "Метод вызывает процедуру копирования данных ценовых уровней."
    )
    @PostMapping("/copy-price-level")
    public void copyPriceLevel(
        @Parameter(description = "Идентификатор партиции", required = true)
        @RequestParam Long partitionId,
        @Parameter(description = "Идентификатор коллекции в которую копируем", required = true)
        @RequestParam Long collectionId,
        @Parameter(description = "Идентификатор группы стран", required = true)
        @RequestParam Long countryGroupId
    ) {
        simple3dPlanService.copyPriceLevel(partitionId, collectionId, countryGroupId);
    }
}
