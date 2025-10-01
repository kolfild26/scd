package ru.sportmaster.scd.repository.adjustment.mapper;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.struct.TpLcMinUpaMSC;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpLcMinUpaMSCMapper extends StructMapper<TpLcMinUpaMSC> {
    public TpLcMinUpaMSCMapper() {
        super(TpLcMinUpaMSC.class);
    }
}
