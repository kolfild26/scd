package ru.sportmaster.scd.repository.httptrace;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.boot.actuate.web.exchanges.HttpExchange;
import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class TraceRepository implements HttpExchangeRepository {
    AtomicReference<HttpExchange> lastTrace = new AtomicReference<>();

    @Override
    public List<HttpExchange> findAll() {
        return Collections.singletonList(lastTrace.get());
    }

    @Override
    public void add(HttpExchange trace) {
        log.debug("HttpEndpoints: {}", traceToString(trace));
        lastTrace.set(trace);
        MDC.clear();
    }

    private String traceToString(HttpExchange trace) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return
            String.format(
                "%s RemoteAddress: %s Uri: %s Principal: %s Status: %s",
                trace.getTimestamp().toString(),
                trace.getRequest().getRemoteAddress(),
                trace.getRequest().getUri().toString(),
                Optional.ofNullable(authentication)
                    .map(Authentication::getPrincipal)
                    .filter(UserDetails.class::isInstance)
                    .map(i -> ((UserDetails) i).getUsername())
                    .orElse("Unauthorized"),
                trace.getResponse().getStatus()
            );
    }
}
