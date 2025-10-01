package ru.sportmaster.scd.repository.adjustment.mapper;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.struct.TpStopListBusinessCountryMSCS;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpStopListBusinessCountryMscsMapper extends StructMapper<TpStopListBusinessCountryMSCS> {
    public TpStopListBusinessCountryMscsMapper() {
        super(TpStopListBusinessCountryMSCS.class);
    }
}
