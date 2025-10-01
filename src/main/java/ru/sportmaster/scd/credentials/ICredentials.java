package ru.sportmaster.scd.credentials;

import ru.sportmaster.scd.entity.User;

/**
 * Параметры входа.
 */
public interface ICredentials {
    String getName();

    User getValue();
}
