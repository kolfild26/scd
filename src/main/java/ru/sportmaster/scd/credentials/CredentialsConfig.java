package ru.sportmaster.scd.credentials;

import lombok.Builder;
import lombok.Getter;
import ru.sportmaster.scd.tokens.IToken;

/**
 * Конфигурация создания параметров входа.
 */
@Builder
@Getter
public class CredentialsConfig {
    private final IToken token;
    private final String usernameKeyName;
    private final String passwordKeyName;
}
