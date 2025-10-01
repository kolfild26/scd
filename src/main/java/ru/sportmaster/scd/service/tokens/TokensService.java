package ru.sportmaster.scd.service.tokens;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.sportmaster.scd.tokens.IToken;

@Slf4j
@Service
public class TokensService implements ITokensService {
    private final Map<String, IToken> tokens;

    public TokensService(@NonNull List<IToken> tokens) {
        this.tokens =
            tokens.stream()
                .collect(Collectors.toMap(
                        IToken::getName,
                        token -> token
                    )
                );
        log.debug("Загружено {} токен(ов).", this.tokens.size());
    }

    @Override
    public Collection<IToken> getTokens() {
        return tokens.values();
    }

    @Override
    public IToken getToken(@NonNull String name) {
        return tokens.get(name);
    }
}
