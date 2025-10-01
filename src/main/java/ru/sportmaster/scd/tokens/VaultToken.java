package ru.sportmaster.scd.tokens;

import static ru.sportmaster.scd.consts.ParamCredentials.BACK_TOKEN_NAME;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.service.tokens.VaultService;

/**
 * Vault токен.
 */
@Component
@ConditionalOnExpression(
    "!'${spring.profiles.active}'.equals('local') or "
        + "('${spring.profiles.active}'.equals('local') and '${scd.env.vault}'.equals('1'))"
)
public class VaultToken extends AbstractVaultToken {
    private static final Long TOKEN_DAY_BEFORE_EXPIRED = 2L;

    public VaultToken(@Value("${scd.token.vault-path}") String vaultPath,
                      @Value("${scd.token.vault-create-orphan}") String createOrphanPath,
                      @Value("${scd.token.vault-polices}") String vaultPolices,
                      @Value("${scd.token.vault-ttl}") String vaultTtl,
                      @Value("${scd.token.path}") String path,
                      @Value("${scd.token.file-name}") String fileName,
                      VaultService vaultService) {
        super(
            BACK_TOKEN_NAME,
            TokenConfig.builder()
                .fileName(path + "/" + fileName)
                .vaultPath(vaultPath)
                .createOrphanPath(createOrphanPath)
                .vaultPolices(vaultPolices)
                .vaultTtl(vaultTtl)
                .dayBeforeExpired(TOKEN_DAY_BEFORE_EXPIRED)
                .build(),
            vaultService
        );
    }
}
