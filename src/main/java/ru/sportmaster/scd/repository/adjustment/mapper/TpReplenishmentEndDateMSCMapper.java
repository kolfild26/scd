package ru.sportmaster.scd.repository.adjustment.mapper;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.struct.TpReplenishmentEndDateStoreMSC;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpReplenishmentEndDateMSCMapper extends StructMapper<TpReplenishmentEndDateStoreMSC> {
    public TpReplenishmentEndDateMSCMapper() {
        super(TpReplenishmentEndDateStoreMSC.class);
    }
}
