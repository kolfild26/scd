package ru.sportmaster.scd.logger;

import static java.util.Optional.ofNullable;
import static ru.sportmaster.scd.consts.ParamNames.ENDPOINT_FIELD;
import static ru.sportmaster.scd.consts.ParamNames.REQUEST_UUID_FIELD;
import static ru.sportmaster.scd.consts.ParamNames.USER_FIELD;
import static ru.sportmaster.scd.web.UserEnvArgumentResolver.buildUserEnv;
import static ru.sportmaster.scd.web.UserEnvArgumentResolver.getOrGenerateRequestUuid;

import jakarta.servlet.http.HttpServletRequest;
import java.net.UnknownHostException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.MDC;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.sportmaster.scd.consts.ParamNames;
import ru.sportmaster.scd.web.UserEnvironment;

@Slf4j
@Aspect
@Component
public class EndpointLoggerAspect {
    @Before("@annotation(ru.sportmaster.scd.logger.EndpointLog)")
    public void fillLoggerContext() {
        HttpServletRequest request = getServletRequest();
        MDC.put(REQUEST_UUID_FIELD, getOrGenerateRequestUuid());
        if (request != null) {
            fillEndpointContext(request);
            fillUserEnv(request);
        }
        fillAuthContext();
        logEndpointRequest();
    }

    private void fillEndpointContext(HttpServletRequest request) {
        MDC.put(ENDPOINT_FIELD, request.getMethod() + " " + request.getRequestURI());
    }

    private void fillAuthContext() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String principalName = (authentication == null) ? "Unauthorized" : authentication.getName();
        MDC.put(USER_FIELD, principalName);
    }

    private void fillUserEnv(HttpServletRequest request) {
        try {
            UserEnvironment env = buildUserEnv(request);
            MDC.put(ParamNames.USER_ID_FIELD, ofNullable(env.getUserId()).map(Object::toString).orElse(null));
            MDC.put(ParamNames.OS_USER_FIELD, env.getOsUser());
            MDC.put(ParamNames.HOST_FIELD, env.getHost());
            MDC.put(ParamNames.IP_ADDRESS_FIELD, env.getIpAddress());
            MDC.put(ParamNames.MODULE_FIELD, env.getModule());
        } catch (UnknownHostException ex) {
            log.error(ex.getMessage());
        }
    }

    private HttpServletRequest getServletRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            return attributes.getRequest();
        }
        return null;
    }

    private void logEndpointRequest() {
        log.debug(
            "Request: {} | UserId: {} OsUser: {} Host: {} IP Address: {} Module: {}",
            MDC.get(ENDPOINT_FIELD),
            MDC.get(ParamNames.USER_ID_FIELD),
            MDC.get(ParamNames.OS_USER_FIELD),
            MDC.get(ParamNames.HOST_FIELD),
            MDC.get(ParamNames.IP_ADDRESS_FIELD),
            MDC.get(ParamNames.MODULE_FIELD)
        );
    }
}
