package ru.sportmaster.scd.repository.adjustment.mapper;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.struct.TpStartListStoreDSC;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpStartListStoreDSCMapper extends StructMapper<TpStartListStoreDSC> {
    public TpStartListStoreDSCMapper() {
        super(TpStartListStoreDSC.class);
    }
}
