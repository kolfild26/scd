package ru.sportmaster.scd.mementity;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.mementity.LocationDepartmentME;

@Component
public class LocationDepartmentMemEntityResolver extends AbstractMemEntityResolver<Long, LocationDepartmentME> {
    private static final String RESOLVER_NAME = "LocationDepartmentMemResolver";

    public LocationDepartmentMemEntityResolver(LocationDepartmentMemEntity memEntity,
                                               ObjectMapper objectMapper,
                                               LocationDepartmentCheckProcessor checkProcessor,
                                               @Value("${scd.memory.entity.expire-hours:72}") long expireHours) {
        super(RESOLVER_NAME, memEntity, objectMapper, checkProcessor, expireHours);
    }

    @Override
    protected Long getKey(@NonNull LocationDepartmentME o) {
        return o.getId();
    }

    @Override
    protected boolean isSearchMatch(LocationDepartmentME obj,
                                    @NonNull String searchText) {
        return
            Optional.ofNullable(obj)
                .map(LocationDepartmentME::toSearchString)
                .map(objText -> objText.toLowerCase().contains(searchText.toLowerCase()))
                .orElse(false);
    }
}
