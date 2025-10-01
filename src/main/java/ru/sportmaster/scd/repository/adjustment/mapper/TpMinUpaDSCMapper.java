package ru.sportmaster.scd.repository.adjustment.mapper;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.struct.TpMinUpaDSC;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpMinUpaDSCMapper extends StructMapper<TpMinUpaDSC> {
    public TpMinUpaDSCMapper() {
        super(TpMinUpaDSC.class);
    }
}
