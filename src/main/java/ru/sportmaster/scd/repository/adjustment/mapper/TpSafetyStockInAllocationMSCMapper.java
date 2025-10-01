package ru.sportmaster.scd.repository.adjustment.mapper;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.struct.TpSafetyStockInAllocationMSC;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpSafetyStockInAllocationMSCMapper extends StructMapper<TpSafetyStockInAllocationMSC> {
    public TpSafetyStockInAllocationMSCMapper() {
        super(TpSafetyStockInAllocationMSC.class);
    }
}
