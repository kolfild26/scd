package ru.sportmaster.scd.repository.adjustment.mapper;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.struct.TpMinUpaMSC;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpMinUpaMSCMapper extends StructMapper<TpMinUpaMSC> {
    public TpMinUpaMSCMapper() {
        super(TpMinUpaMSC.class);
    }
}
