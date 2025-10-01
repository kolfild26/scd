package ru.sportmaster.scd.web;

import static ru.sportmaster.scd.consts.ParamNames.REQUEST_UUID_FIELD;

import jakarta.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import ru.sportmaster.scd.entity.User;

public class UserEnvArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(UserEnv.class) != null;
    }

    @Override
    public Object resolveArgument(@NotNull MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws UnknownHostException {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        return buildUserEnv(request);
    }

    public static UserEnvironment buildUserEnv(HttpServletRequest request) throws UnknownHostException {
        InetAddress address = InetAddress.getByName(request.getRemoteHost());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Long userId = null;
        String osUser = "Unauthorized";
        if (isAuthenticated(authentication)) {
            User user = (User) authentication.getPrincipal();
            userId = user.getId();
            osUser = user.getLogin();
        }

        return UserEnvironment.builder()
            .requestUuid(getOrGenerateRequestUuid())
            .userId(userId)
            .osUser(osUser)
            .host(getHost(request, address))
            .ipAddress(address.getHostAddress())
            .module(request.getHeader("x-module"))
            .build();
    }

    public static String getOrGenerateRequestUuid() {
        String uuid = MDC.get(REQUEST_UUID_FIELD);
        if (uuid == null) {
            uuid = UUID.randomUUID().toString();
            MDC.put(REQUEST_UUID_FIELD, uuid);
        }
        return uuid;
    }

    private static String getHost(HttpServletRequest request, InetAddress address) throws UnknownHostException {
        boolean isLocalhost = request.getRemoteAddr().equalsIgnoreCase(InetAddress.getLocalHost().getHostAddress());
        return isLocalhost ? "localhost" : address.getCanonicalHostName();
    }

    private static boolean isAuthenticated(Authentication authentication) {
        return !(authentication instanceof AnonymousAuthenticationToken);
    }
}
