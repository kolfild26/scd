package ru.sportmaster.scd.repository.adjustment.mapper;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.struct.TpStopListStoreMSC;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpStopListStoreMSCMapper extends StructMapper<TpStopListStoreMSC> {
    public TpStopListStoreMSCMapper() {
        super(TpStopListStoreMSC.class);
    }
}
