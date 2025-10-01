package ru.sportmaster.scd.credentials;

import static ru.sportmaster.scd.consts.ParamCredentials.BACK_TOKEN_NAME;
import static ru.sportmaster.scd.consts.ParamCredentials.DATABASE_CREDENTIAL_NAME;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.service.tokens.ITokensService;

/**
 * Параметры входа для базы данных.
 */
@Component
public class DatabaseCredentials extends Credentials {
    public DatabaseCredentials(@Value("${scd.token.vault-database-user}") String databaseUser,
                               @Value("${scd.token.vault-database-password}") String databasePassword,
                               ITokensService tokensService) {
        super(
            DATABASE_CREDENTIAL_NAME,
            CredentialsConfig.builder()
                .token(tokensService.getToken(BACK_TOKEN_NAME))
                .usernameKeyName(databaseUser)
                .passwordKeyName(databasePassword)
                .build()
        );
    }
}
