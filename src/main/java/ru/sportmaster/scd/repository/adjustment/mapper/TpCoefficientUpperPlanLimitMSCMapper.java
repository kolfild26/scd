package ru.sportmaster.scd.repository.adjustment.mapper;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.struct.TpCoefficientUpperPlanLimitMSC;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpCoefficientUpperPlanLimitMSCMapper extends StructMapper<TpCoefficientUpperPlanLimitMSC> {
    public TpCoefficientUpperPlanLimitMSCMapper() {
        super(TpCoefficientUpperPlanLimitMSC.class);
    }
}
