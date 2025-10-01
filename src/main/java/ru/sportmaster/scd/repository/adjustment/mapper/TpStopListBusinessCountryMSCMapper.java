package ru.sportmaster.scd.repository.adjustment.mapper;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.struct.TpStopListBusinessCountryMSC;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpStopListBusinessCountryMSCMapper extends StructMapper<TpStopListBusinessCountryMSC> {
    public TpStopListBusinessCountryMSCMapper() {
        super(TpStopListBusinessCountryMSC.class);
    }
}
