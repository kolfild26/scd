package ru.sportmaster.scd.repository.adjustment.mapper;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.struct.TpStopListBusinessCountryDSC;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpStopListBusinessCountryDSCMapper extends StructMapper<TpStopListBusinessCountryDSC> {
    public TpStopListBusinessCountryDSCMapper() {
        super(TpStopListBusinessCountryDSC.class);
    }
}
