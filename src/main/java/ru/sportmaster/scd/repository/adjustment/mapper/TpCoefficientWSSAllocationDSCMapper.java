package ru.sportmaster.scd.repository.adjustment.mapper;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.struct.TpCoefficientWSSAllocationDSC;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpCoefficientWSSAllocationDSCMapper extends StructMapper<TpCoefficientWSSAllocationDSC> {
    public TpCoefficientWSSAllocationDSCMapper() {
        super(TpCoefficientWSSAllocationDSC.class);
    }
}
