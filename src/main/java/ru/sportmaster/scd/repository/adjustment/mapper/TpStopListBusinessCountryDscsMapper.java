package ru.sportmaster.scd.repository.adjustment.mapper;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.struct.TpStopListBusinessCountryDSCS;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpStopListBusinessCountryDscsMapper extends StructMapper<TpStopListBusinessCountryDSCS> {
    public TpStopListBusinessCountryDscsMapper() {
        super(TpStopListBusinessCountryDSCS.class);
    }
}
