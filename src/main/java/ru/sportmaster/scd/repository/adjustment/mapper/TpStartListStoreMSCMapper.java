package ru.sportmaster.scd.repository.adjustment.mapper;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.struct.TpStartListStoreMSC;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpStartListStoreMSCMapper extends StructMapper<TpStartListStoreMSC> {
    public TpStartListStoreMSCMapper() {
        super(TpStartListStoreMSC.class);
    }
}
