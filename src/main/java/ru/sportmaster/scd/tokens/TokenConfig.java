package ru.sportmaster.scd.tokens;

import lombok.Builder;
import lombok.Getter;

/**
 * Конфигурация создания токена.
 */
@Builder
@Getter
public class TokenConfig {
    private final String fileName;
    private final String vaultPath;
    private final String createOrphanPath;
    private final String vaultPolices;
    private final String vaultTtl;
    private final Long dayBeforeExpired;
}
