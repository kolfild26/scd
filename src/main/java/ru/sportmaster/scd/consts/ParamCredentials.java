package ru.sportmaster.scd.consts;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * Константные значения для обеспечения работы с токенами и параметрами входа.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ParamCredentials {
    public static final String AIRFLOW_CREDENTIAL_NAME = "AirFlowCredential";
    public static final String DATABASE_CREDENTIAL_NAME = "DataBaseCredential";
    public static final String BACK_TOKEN_NAME = "TokenForBack";

    public static final String LEASE_DURATION_NAME = "lease_duration";
    public static final String DATA_NAME = "data";

    public static final String ERROR_RESPONSE_NAME = "errors";
    public static final String AUTH_RESPONSE_NAME = "auth";
    public static final String CLIENT_TOKEN_RESPONSE_NAME = "client_token";
}
