package ru.sportmaster.scd.mementity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.mementity.MerchWareME;
import ru.sportmaster.scd.repository.mementity.MerchWareMECheckProcessorRepository;
import ru.sportmaster.scd.utils.ConvertUtil;

@Component
@ConditionalOnExpression("${scd.memory.entity.merch-use:true}")
public class MerchWareCheckProcessor extends MemEntityCheckProcessor<Long, MerchWareME> {
    public MerchWareCheckProcessor(MerchWareMECheckProcessorRepository checkProcessorRepository,
                                   @Value("${scd.memory.entity.expire-hours:72}") long expireHours) {
        super(ConvertUtil::getLongValue, checkProcessorRepository, expireHours);
    }
}
