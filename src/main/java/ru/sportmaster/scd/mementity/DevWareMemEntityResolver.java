package ru.sportmaster.scd.mementity;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.mementity.DevWareME;

@Component
@ConditionalOnExpression("${scd.memory.entity.dev-use:true}")
public class DevWareMemEntityResolver extends AbstractMemEntityResolver<Long, DevWareME> {
    private static final String RESOLVER_NAME = "DevWareMemResolver";

    public DevWareMemEntityResolver(DevWareMemEntity memEntity,
                                    ObjectMapper objectMapper,
                                    DevWareCheckProcessor checkProcessor,
                                    @Value("${scd.memory.entity.expire-hours:72}") long expireHours) {
        super(RESOLVER_NAME, memEntity, objectMapper, checkProcessor, expireHours);
    }

    @Override
    protected Long getKey(@NonNull DevWareME o) {
        return o.getId();
    }

    @Override
    protected boolean isSearchMatch(DevWareME obj,
                                    @NonNull String searchText) {
        return
            Optional.ofNullable(obj)
                .map(DevWareME::toSearchString)
                .map(objText -> objText.toLowerCase().contains(searchText.toLowerCase()))
                .orElse(false);
    }
}
