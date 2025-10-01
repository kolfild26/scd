package ru.sportmaster.scd.repository.adjustment.mapper;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.struct.TpSafetyStockInAllocationDSC;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpSafetyStockInAllocationDSCMapper extends StructMapper<TpSafetyStockInAllocationDSC> {
    public TpSafetyStockInAllocationDSCMapper() {
        super(TpSafetyStockInAllocationDSC.class);
    }
}
