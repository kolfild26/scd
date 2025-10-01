package ru.sportmaster.scd.service.tokens;

import java.util.Collection;
import org.springframework.lang.NonNull;
import ru.sportmaster.scd.tokens.IToken;

/**
 * Сервис доступа к токенам.
 */
public interface ITokensService {
    Collection<IToken> getTokens();

    IToken getToken(@NonNull String name);
}
