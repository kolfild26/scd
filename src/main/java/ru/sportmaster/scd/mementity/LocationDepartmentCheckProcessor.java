package ru.sportmaster.scd.mementity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.mementity.LocationDepartmentME;
import ru.sportmaster.scd.repository.mementity.LocationDepartmentMECheckProcessorRepository;
import ru.sportmaster.scd.utils.ConvertUtil;

@Component
public class LocationDepartmentCheckProcessor extends MemEntityCheckProcessor<Long, LocationDepartmentME> {
    public LocationDepartmentCheckProcessor(LocationDepartmentMECheckProcessorRepository checkProcessorRepository,
                                            @Value("${scd.memory.entity.expire-hours:72}") long expireHours) {
        super(ConvertUtil::getLongValue, checkProcessorRepository, expireHours);
    }
}
