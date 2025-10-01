package ru.sportmaster.scd.service.credentials;

import org.springframework.lang.NonNull;
import ru.sportmaster.scd.credentials.ICredentials;

/**
 * Сервис доступа к параметрам входа.
 */
public interface ICredentialsService {
    ICredentials getCredential(@NonNull String name);
}
