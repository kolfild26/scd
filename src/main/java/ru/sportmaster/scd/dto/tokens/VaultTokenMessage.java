package ru.sportmaster.scd.dto.tokens;

import java.io.Serial;
import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

/**
 * Сообщение для обмена командами по работе с токенами между членами кластера.
 */
@Slf4j
@Builder
@Getter
@ToString
public class VaultTokenMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 5732107117953030409L;

    private Command command;
    private String params;

    public static VaultTokenMessage createLoadMessage(@NonNull String name) {
        return VaultTokenMessage.builder().command(Command.LOAD).params(name).build();
    }

    public enum Command {
        LOAD
    }
}
