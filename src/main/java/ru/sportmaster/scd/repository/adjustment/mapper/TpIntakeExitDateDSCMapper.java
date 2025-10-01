package ru.sportmaster.scd.repository.adjustment.mapper;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.struct.TpIntakeExitDateDSC;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpIntakeExitDateDSCMapper extends StructMapper<TpIntakeExitDateDSC> {
    public TpIntakeExitDateDSCMapper() {
        super(TpIntakeExitDateDSC.class);
    }
}
