package ru.sportmaster.scd.tokens;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Токен доступа.
 */
public interface IToken {
    String getName();

    JsonNode getSecret() throws JsonProcessingException;

    void loadToken();

    boolean isChangeToken();
}
