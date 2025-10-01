package ru.sportmaster.scd.repository.adjustment.mapper;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.struct.TpLcDatesDSC;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpLcDatesDSCMapper extends StructMapper<TpLcDatesDSC> {
    public TpLcDatesDSCMapper() {
        super(TpLcDatesDSC.class);
    }
}
