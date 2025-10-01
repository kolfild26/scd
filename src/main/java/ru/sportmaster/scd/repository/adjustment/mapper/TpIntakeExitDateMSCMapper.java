package ru.sportmaster.scd.repository.adjustment.mapper;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.struct.TpIntakeExitDateMSC;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpIntakeExitDateMSCMapper extends StructMapper<TpIntakeExitDateMSC> {
    public TpIntakeExitDateMSCMapper() {
        super(TpIntakeExitDateMSC.class);
    }
}
