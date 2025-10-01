package ru.sportmaster.scd.mementity;

import static ru.sportmaster.scd.consts.ParamNames.MERCH_WARE_ENTITY_NAME;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.mementity.MerchWareME;
import ru.sportmaster.scd.repository.mementity.MerchWareMERepository;

@Component
@ConditionalOnExpression("${scd.memory.entity.merch-use:true}")
public class MerchWareMemEntity extends AbstractMemEntity<Long, MerchWareME> {
    private static final String MAP_NAME = "MerchWareMapEntity";

    public MerchWareMemEntity(@Value("${scd.memory.entity.minute-reload:360}") Long reloadPeriod,
                              MerchWareMERepository repository) {
        super(MAP_NAME, repository, MERCH_WARE_ENTITY_NAME, reloadPeriod);
    }
}
