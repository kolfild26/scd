package ru.sportmaster.scd;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.mementity.MemEntityTopic;
import ru.sportmaster.scd.service.mementity.MemEntityInitialization;
import ru.sportmaster.scd.service.task.TaskManager;
import ru.sportmaster.scd.service.tokens.VaultTokenUpdateService;
import ru.sportmaster.scd.task.HazelCastComponent;
import ru.sportmaster.scd.ui.view.UiViewLoader;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {
    private final UiViewLoader uiViewLoader;
    private final HazelCastComponent hazelCastComponent;
    private final MemEntityInitialization memEntityInitialization;
    private final MemEntityTopic memEntityTopic;
    private final TaskManager taskManager;
    private final VaultTokenUpdateService vaultTokenUpdateService;

    @Override
    public void onApplicationEvent(@NotNull ApplicationReadyEvent event) {
        final String initExecuteSample = "Инициализация {} выполнена.";
        log.debug("Приложение настроено. Инициализация.");
        hazelCastComponent.init();
        log.debug(initExecuteSample,"hazelCastComponent");
        taskManager.init();
        log.debug(initExecuteSample,"taskManager");
        memEntityInitialization.init();
        log.debug(initExecuteSample,"memEntityInitialization");
        memEntityTopic.init(hazelCastComponent.getHazelcastInstance());
        log.debug(initExecuteSample, "memEntityTopic");
        uiViewLoader.init();
        log.debug(initExecuteSample,"uiViewLoader");
        vaultTokenUpdateService.init();
        log.debug(initExecuteSample,"vaultTokenUpdateService");
        log.debug("Инициализация закончена.");
    }
}
