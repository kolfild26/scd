package ru.sportmaster.scd.repository.adjustment.mapper;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.struct.TpReplenishmentEndDateSalesChannelMSC;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpReplenishmentEndDateSalesChannelMSCMapper extends StructMapper<TpReplenishmentEndDateSalesChannelMSC> {
    public TpReplenishmentEndDateSalesChannelMSCMapper() {
        super(TpReplenishmentEndDateSalesChannelMSC.class);
    }
}
