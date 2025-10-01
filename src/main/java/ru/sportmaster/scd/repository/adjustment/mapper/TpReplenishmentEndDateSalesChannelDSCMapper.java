package ru.sportmaster.scd.repository.adjustment.mapper;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.struct.TpReplenishmentEndDateSalesChannelDSC;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpReplenishmentEndDateSalesChannelDSCMapper extends StructMapper<TpReplenishmentEndDateSalesChannelDSC> {
    public TpReplenishmentEndDateSalesChannelDSCMapper() {
        super(TpReplenishmentEndDateSalesChannelDSC.class);
    }
}
