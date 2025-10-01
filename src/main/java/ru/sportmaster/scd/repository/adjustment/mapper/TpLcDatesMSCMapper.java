package ru.sportmaster.scd.repository.adjustment.mapper;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.struct.TpLcDatesMSC;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpLcDatesMSCMapper extends StructMapper<TpLcDatesMSC> {
    public TpLcDatesMSCMapper() {
        super(TpLcDatesMSC.class);
    }
}
