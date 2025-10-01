package ru.sportmaster.scd.mementity;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.mementity.MerchWareME;

@Component
@ConditionalOnExpression("${scd.memory.entity.merch-use:true}")
public class MerchWareMemEntityResolver extends AbstractMemEntityResolver<Long, MerchWareME> {
    private static final String RESOLVER_NAME = "MerchWareMemResolver";

    public MerchWareMemEntityResolver(MerchWareMemEntity memEntity,
                                      ObjectMapper objectMapper,
                                      MerchWareCheckProcessor checkProcessor,
                                      @Value("${scd.memory.entity.expire-hours:72}") long expireHours) {
        super(RESOLVER_NAME, memEntity, objectMapper, checkProcessor, expireHours);
    }

    @Override
    protected Long getKey(@NonNull MerchWareME o) {
        return o.getId();
    }

    @Override
    protected boolean isSearchMatch(MerchWareME obj,
                                    @NonNull String searchText) {
        return
            Optional.ofNullable(obj)
                .map(MerchWareME::toSearchString)
                .map(objText -> objText.toLowerCase().contains(searchText.toLowerCase()))
                .orElse(false);
    }
}
