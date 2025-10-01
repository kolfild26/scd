package ru.sportmaster.scd.mementity;

import static ru.sportmaster.scd.consts.ParamNames.DEV_WARE_ENTITY_NAME;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.mementity.DevWareME;
import ru.sportmaster.scd.repository.mementity.DevWareMERepository;

@Component
@ConditionalOnExpression("${scd.memory.entity.dev-use:true}")
public class DevWareMemEntity extends AbstractMemEntity<Long, DevWareME> {
    private static final String MAP_NAME = "DevWareMapEntity";

    public DevWareMemEntity(@Value("${scd.memory.entity.minute-reload:360}") Long reloadPeriod,
                            DevWareMERepository repository) {
        super(MAP_NAME, repository, DEV_WARE_ENTITY_NAME, reloadPeriod);
    }
}
