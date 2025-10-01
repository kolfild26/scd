package ru.sportmaster.scd.repository.adjustment.mapper;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.struct.TpReplenishmentStrategyDSC;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpReplenishmentStrategyDSCMapper extends StructMapper<TpReplenishmentStrategyDSC> {
    public TpReplenishmentStrategyDSCMapper() {
        super(TpReplenishmentStrategyDSC.class);
    }
}
