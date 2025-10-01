package ru.sportmaster.scd.repository.adjustment.mapper;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.struct.TpLcMinUpaDSC;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpLcMinUpaDSCMapper extends StructMapper<TpLcMinUpaDSC> {
    public TpLcMinUpaDSCMapper() {
        super(TpLcMinUpaDSC.class);
    }
}
