package ru.sportmaster.scd.tokens;

import static ru.sportmaster.scd.consts.ParamCredentials.BACK_TOKEN_NAME;
import static ru.sportmaster.scd.consts.ParamCredentials.DATA_NAME;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnExpression("'${spring.profiles.active}'.equals('local') and !'${scd.env.vault}'.equals('1')")
public class LocalToken implements IToken {
    private static final String JSON_SAMPLE = "{\"%s\": {\"%s\": \"%s\", \"%s\": \"%s\"}}";
    private static final String NAME = BACK_TOKEN_NAME;
    @Value("${scd.token.vault-database-user}")
    private String databaseUserKey;
    @Value("${scd.token.vault-database-password}")
    private String databasePasswordKey;
    @Value("${scd.db.username}")
    private String databaseUser;
    @Value("${scd.db.password}")
    private String databasePassword;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public JsonNode getSecret() throws JsonProcessingException {
        return
            mapper
                .readTree(
                    JSON_SAMPLE.formatted(
                        DATA_NAME,
                        databaseUserKey,
                        databaseUser,
                        databasePasswordKey,
                        databasePassword
                    )
                );
    }

    @Override
    public void loadToken() {
        // Токен не сохраняется
    }

    @Override
    public boolean isChangeToken() {
        return false;
    }
}
