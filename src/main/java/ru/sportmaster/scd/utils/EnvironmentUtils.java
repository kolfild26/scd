package ru.sportmaster.scd.utils;

import static ru.sportmaster.scd.consts.ParamNames.ENDPOINT_FIELD;
import static ru.sportmaster.scd.consts.ParamNames.HOST_FIELD;
import static ru.sportmaster.scd.consts.ParamNames.IP_ADDRESS_FIELD;
import static ru.sportmaster.scd.consts.ParamNames.MODULE_FIELD;
import static ru.sportmaster.scd.consts.ParamNames.OS_USER_FIELD;
import static ru.sportmaster.scd.consts.ParamNames.REQUEST_UUID_FIELD;
import static ru.sportmaster.scd.consts.ParamNames.USER_FIELD;
import static ru.sportmaster.scd.consts.ParamNames.USER_ID_FIELD;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import ru.sportmaster.scd.SCDApplication;

/**
 * Служебный класс, предоставляющий функционал работы с окружением.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EnvironmentUtils {
    private static final String LOCAL_HOST = "localhost";

    /**
     * Получение имени пользователя операционной системы.
     *
     * @return - имя пользователя
     */
    public static String getOsUserName() {
        String osName = System.getProperty("os.name").toLowerCase();
        String className = null;
        String methodName = "getUsername";

        if (osName.contains("windows")) {
            className = "com.sun.security.auth.module.NTSystem";
            methodName = "getName";
        } else if (osName.contains("linux")) {
            className = "com.sun.security.auth.module.UnixSystem";
        } else if (osName.contains("solaris") || osName.contains("sunos")) {
            className = "com.sun.security.auth.module.SolarisSystem";
        }

        if (className != null) {
            try {
                var clazz = Class.forName(className);
                Method method = clazz.getDeclaredMethod(methodName);
                Object o = clazz.getDeclaredConstructor().newInstance();
                return (String) method.invoke(o);
            } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException
                     | InstantiationException | IllegalAccessException ignored) {
                return null;
            }
        }
        return null;
    }

    /**
     * Формирование параметров окружения.
     *
     * @return - параметры окружения
     */
    public static Map<String, String> getEnvironmentValues() {
        Map<String, String> result = new HashMap<>();
        result.put(
            REQUEST_UUID_FIELD,
            Optional.ofNullable(MDC.get(REQUEST_UUID_FIELD))
                .orElse(UUID.randomUUID().toString())
        );
        result.put(ENDPOINT_FIELD, MDC.get(ENDPOINT_FIELD));
        result.put(
            USER_FIELD,
            Optional.ofNullable(MDC.get(USER_FIELD))
                .orElse(EnvironmentUtils.getOsUserName())
        );
        result.put(USER_ID_FIELD, MDC.get(USER_ID_FIELD));
        result.put(
            OS_USER_FIELD,
            Optional.ofNullable(MDC.get(OS_USER_FIELD))
                .orElse(result.get(USER_FIELD))
        );
        result.put(
            HOST_FIELD,
            Optional.ofNullable(MDC.get(HOST_FIELD))
                .orElse(LOCAL_HOST)
        );
        try {
            result.put(
                IP_ADDRESS_FIELD,
                Optional.ofNullable(MDC.get(IP_ADDRESS_FIELD))
                    .orElse(InetAddress.getLocalHost().getHostAddress())
            );
        } catch (UnknownHostException e) {
            result.put(
                IP_ADDRESS_FIELD,
                LOCAL_HOST
            );
        }
        result.put(
            MODULE_FIELD,
            Optional.ofNullable(MDC.get(MODULE_FIELD))
                .orElse(SCDApplication.class.getCanonicalName())
        );
        return result;
    }

    /**
     * Восстановление параметров окружения из сформированных.
     *
     * @param environmentValues - сформированные параметры окружения
     */
    public static void restoreEnvironment(@NonNull Map<String, String> environmentValues) {
        MDC.clear();
        environmentValues.forEach(MDC::put);
    }
}
