package ru.sportmaster.scd.tokens;

import static java.util.Objects.nonNull;

import com.hazelcast.topic.Message;
import com.hazelcast.topic.MessageListener;
import lombok.extern.slf4j.Slf4j;
import ru.sportmaster.scd.dto.tokens.VaultTokenMessage;
import ru.sportmaster.scd.service.tokens.ITokensService;
import ru.sportmaster.scd.utils.BeanUtil;

/**
 * Обработчик команд работы с токенами на кластере.
 */
@Slf4j
public class VaultTokenMessageListener implements MessageListener<VaultTokenMessage> {
    @Override
    public void onMessage(Message<VaultTokenMessage> message) {
        var vaultMessage = message.getMessageObject();
        log.debug("Получено сообщение vault: {}", vaultMessage);
        if (VaultTokenMessage.Command.LOAD.equals(vaultMessage.getCommand())) {
            var tokensService = BeanUtil.getBean(ITokensService.class);
            var token = tokensService.getToken(vaultMessage.getParams());
            if (nonNull(token)) {
                token.loadToken();
            }
        }
    }
}
