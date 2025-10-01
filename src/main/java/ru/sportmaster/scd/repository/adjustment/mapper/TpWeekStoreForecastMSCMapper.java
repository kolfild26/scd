package ru.sportmaster.scd.repository.adjustment.mapper;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.struct.TpWeekStoreForecastMSC;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpWeekStoreForecastMSCMapper extends StructMapper<TpWeekStoreForecastMSC> {
    public TpWeekStoreForecastMSCMapper() {
        super(TpWeekStoreForecastMSC.class);
    }
}