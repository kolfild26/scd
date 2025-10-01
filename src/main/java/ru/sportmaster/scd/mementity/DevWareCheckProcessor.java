package ru.sportmaster.scd.mementity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.mementity.DevWareME;
import ru.sportmaster.scd.repository.mementity.DevWareMECheckProcessorRepository;
import ru.sportmaster.scd.utils.ConvertUtil;

@Component
@ConditionalOnExpression("${scd.memory.entity.dev-use:true}")
public class DevWareCheckProcessor extends MemEntityCheckProcessor<Long, DevWareME> {
    public DevWareCheckProcessor(DevWareMECheckProcessorRepository checkProcessorRepository,
                                 @Value("${scd.memory.entity.expire-hours:72}") long expireHours) {
        super(ConvertUtil::getLongValue, checkProcessorRepository,expireHours);
    }
}
