package ru.sportmaster.scd.repository.adjustment.mapper;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.struct.TpStopListStoreDSC;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpStopListStoreDSCMapper extends StructMapper<TpStopListStoreDSC> {
    public TpStopListStoreDSCMapper() {
        super(TpStopListStoreDSC.class);
    }
}
