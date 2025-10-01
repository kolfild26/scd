package ru.sportmaster.scd.credentials;

import static ru.sportmaster.scd.consts.ParamCredentials.AIRFLOW_CREDENTIAL_NAME;
import static ru.sportmaster.scd.consts.ParamCredentials.BACK_TOKEN_NAME;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.service.tokens.ITokensService;

/**
 * Параметры входа для работы с AirFlow.
 */
@Component
@ConditionalOnExpression(
    "!'${spring.profiles.active}'.equals('local') or "
        + "('${spring.profiles.active}'.equals('local') and '${scd.env.vault}'.equals('1'))"
)
public class AirflowCredentials extends Credentials {
    public AirflowCredentials(@Value("${scd.token.vault-airflow-user}") String databaseUser,
                              @Value("${scd.token.vault-airflow-password}") String databasePassword,
                              ITokensService tokensService) {
        super(
            AIRFLOW_CREDENTIAL_NAME,
            CredentialsConfig.builder()
                .token(tokensService.getToken(BACK_TOKEN_NAME))
                .usernameKeyName(databaseUser)
                .passwordKeyName(databasePassword)
                .build()
        );
    }
}
