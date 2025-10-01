package ru.sportmaster.scd.tokens;

import static ru.sportmaster.scd.utils.EnvironmentUtils.restoreEnvironment;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import ru.sportmaster.scd.dto.tokens.VaultTokenMessage;
import ru.sportmaster.scd.exceptions.VaultException;
import ru.sportmaster.scd.service.tokens.ITokensService;
import ru.sportmaster.scd.task.HazelCastComponent;
import ru.sportmaster.scd.utils.BeanUtil;

/**
 * Задание на обновление токена.
 */
@Slf4j
@RequiredArgsConstructor
public class VaultUpdateTokenTask implements Runnable, Serializable {
    @Serial
    private static final long serialVersionUID = 2180486548735741254L;

    private final Map<String,String> environment;

    @Override
    public void run() {
        restoreEnvironment(environment);
        log.info("Запуск процесса проверки срока действия токенов.");
        try {
            var tokensService =
                Optional.ofNullable(BeanUtil.getBean(ITokensService.class))
                    .orElseThrow(() -> new VaultException("Инициализация приложения не закончена."));
            var tokens = tokensService.getTokens();
            for (var token : tokens) {
                log.debug("Проверка токена для {}", token.getName());
                if (token.isChangeToken()) {
                    loadToken(token.getName());
                }
            }
            log.info("Процесс проверки срока действия токенов завершен.");
        } catch (Exception e) {
            log.error("Ошибка процесса проверки срока действия токенов: {}", e.getMessage(), e);
        }
    }

    private void loadToken(@NonNull String name) {
        log.debug("Отправляем команду обновления токена для {}", name);
        var hazelCastComponent = BeanUtil.getBean(HazelCastComponent.class);
        hazelCastComponent
            .getVaultClusterTopic()
            .publish(
                VaultTokenMessage.createLoadMessage(name)
            );
    }
}
