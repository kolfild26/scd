package ru.sportmaster.scd.service.tokens;

import static ru.sportmaster.scd.utils.EnvironmentUtils.getEnvironmentValues;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.sportmaster.scd.task.HazelCastComponent;
import ru.sportmaster.scd.tokens.VaultUpdateTokenTask;

/**
 * Сервис обновления и продления токенов.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VaultTokenUpdateService {
    private final HazelCastComponent hazelCastComponent;

    public void init() {
        log.debug("Инициализация сервиса обновления и продления токенов.");
        if (hazelCastComponent.isFirstClusterMember()) {
            var executor = hazelCastComponent.getVaultScheduledExec();
            executor.scheduleAtFixedRate(
                    new VaultUpdateTokenTask(getEnvironmentValues()),
                    0,
                    1,
                    TimeUnit.DAYS
            );
            log.debug("Сервис обновления и продления токенов запущен.");
        }
    }
}
