package ru.sportmaster.scd.repository.adjustment.mapper;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.struct.TpPlanLimitMSC;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpPlanLimitMSCMapper extends StructMapper<TpPlanLimitMSC> {
    public TpPlanLimitMSCMapper() {
        super(TpPlanLimitMSC.class);
    }
}
