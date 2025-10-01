package ru.sportmaster.scd.repository.adjustment.mapper;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.struct.TpCoefficientWSSAllocationMSC;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpCoefficientWSSAllocationMSCMapper extends StructMapper<TpCoefficientWSSAllocationMSC> {
    public TpCoefficientWSSAllocationMSCMapper() {
        super(TpCoefficientWSSAllocationMSC.class);
    }
}
