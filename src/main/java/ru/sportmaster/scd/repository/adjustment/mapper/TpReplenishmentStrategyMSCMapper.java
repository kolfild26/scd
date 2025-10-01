package ru.sportmaster.scd.repository.adjustment.mapper;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.struct.TpReplenishmentStrategyMSC;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpReplenishmentStrategyMSCMapper extends StructMapper<TpReplenishmentStrategyMSC> {
    public TpReplenishmentStrategyMSCMapper() {
        super(TpReplenishmentStrategyMSC.class);
    }
}
