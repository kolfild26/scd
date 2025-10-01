package ru.sportmaster.scd.repository.adjustment.mapper;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.struct.TpCoefficientUpperPlanLimitDSC;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpCoefficientUpperPlanLimitDSCMapper extends StructMapper<TpCoefficientUpperPlanLimitDSC> {
    public TpCoefficientUpperPlanLimitDSCMapper() {
        super(TpCoefficientUpperPlanLimitDSC.class);
    }
}
