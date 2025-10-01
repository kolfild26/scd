package ru.sportmaster.scd.controller;

import static ru.sportmaster.scd.config.OpenApiConfig.AUTH_NAME;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.sportmaster.scd.dto.settings.AlgSettingsStatus;
import ru.sportmaster.scd.dto.settings.AlgType;
import ru.sportmaster.scd.entity.dictionary.Collection;
import ru.sportmaster.scd.logger.EndpointLog;
import ru.sportmaster.scd.service.planlimit.PlanLimitSettingsService;
import ru.sportmaster.scd.service.replenishment.ReplSettingsService;
import ru.sportmaster.scd.service.settings.AlgSettingLockService;

@Slf4j
@Tag(name = "Algorithm Settings")
@RestController
@RequestMapping("${scd.base-path}/alg-settings")
@RequiredArgsConstructor
@SecurityRequirement(name = AUTH_NAME)
public class AlgSettingsController {
    private final AlgSettingLockService algSettingLockService;
    private final ReplSettingsService replSettingsService;
    private final PlanLimitSettingsService planLimitSettingsService;

    @EndpointLog
    @Operation(
        summary = "Получение статуса визарда настроек стратегий пополнения.",
        description = "Метод возвращает статус визарда настроек стратегий пополнения, "
            + "со статусом редактирования или запущенного алгоритма."
    )
    @GetMapping("/info")
    public AlgSettingsStatus getAlgSettingsInfo(
        @Parameter(description = "Тип алгоритма", required = true)
        @RequestParam AlgType type
    ) {
        return algSettingLockService.getAlgSettingsInfo(type);
    }

    @EndpointLog
    @Operation(
        summary = "Блокирование формы настроек стратегий пополнения",
        description = "Метод для блокировки визарда настроек стратегий пополнения, "
            + "во время работы с настройками другим пользователем."
    )
    @PostMapping("/lock")
    public void lock(
        @Parameter(description = "Тип алгоритма", required = true)
        @RequestParam AlgType type
    ) {
        algSettingLockService.lock(type);
    }

    @EndpointLog
    @Operation(
        summary = "Разблокировка формы настроек стратегий пополнения",
        description = "Метод для разблокировки визарда настроек стратегий пополнения, "
            + "после окончания работы с настройками пользоваталем."
    )
    @PostMapping("/unlock")
    public void unlock(
        @Parameter(description = "Тип алгоритма", required = true)
        @RequestParam AlgType type
    ) {
        algSettingLockService.unlock(type);
    }

    @EndpointLog
    @Operation(
        summary = "Получение доступных значений для дерева аттрибутов КГТ",
        description = "Метод ддя получения доступных значений дерева аттрибутов КГТ, "
            + "для настроек стратегий пополнения"
    )
    @GetMapping("/repl/kgt-tree-values")
    public List<ObjectNode> getKgtAttrTreeValues(
        @Parameter(description = "Уровень дерева", required = true)
        @RequestParam String level,
        @Parameter(description = "Идентификатор параметра")
        @RequestParam(required = false) Long id) {
        return replSettingsService.getKgtAttrTreeValues(level, id);
    }

    @EndpointLog
    @Operation(
        summary = "Получение доступных коллекций с настройками плановых лимитов.",
        description = "Метод для получения доступных коллекций с настройками плановых лимитов."
    )
    @GetMapping("/pl/available-coll")
    public List<Collection> getPlAvailableCollections(
        @Parameter(description = "Уровень дерева", required = true)
        @RequestParam Long divisionId
    ) {
        return planLimitSettingsService.getAvailableCollections(divisionId);
    }
}
