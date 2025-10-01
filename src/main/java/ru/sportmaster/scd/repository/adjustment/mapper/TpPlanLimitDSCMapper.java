package ru.sportmaster.scd.repository.adjustment.mapper;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.struct.TpPlanLimitDSC;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpPlanLimitDSCMapper extends StructMapper<TpPlanLimitDSC> {
    public TpPlanLimitDSCMapper() {
        super(TpPlanLimitDSC.class);
    }
}
