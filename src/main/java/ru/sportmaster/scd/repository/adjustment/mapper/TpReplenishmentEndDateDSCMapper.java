package ru.sportmaster.scd.repository.adjustment.mapper;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.struct.TpReplenishmentEndDateStoreDSC;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpReplenishmentEndDateDSCMapper extends StructMapper<TpReplenishmentEndDateStoreDSC> {
    public TpReplenishmentEndDateDSCMapper() {
        super(TpReplenishmentEndDateStoreDSC.class);
    }
}
