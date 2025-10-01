package ru.sportmaster.scd.service.credentials;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.sportmaster.scd.credentials.ICredentials;

@Slf4j
@Service
public class CredentialsService implements ICredentialsService {
    private final Map<String, ICredentials> credentials;

    public CredentialsService(@NonNull List<ICredentials> credentials) {
        this.credentials =
            credentials.stream()
                .collect(Collectors.toMap(
                        ICredentials::getName,
                        token -> token
                    )
                );
        log.debug("Загружено {} реквизитов для входа.", this.credentials.size());
    }

    @Override
    public ICredentials getCredential(@NonNull String name) {
        return credentials.get(name);
    }
}
