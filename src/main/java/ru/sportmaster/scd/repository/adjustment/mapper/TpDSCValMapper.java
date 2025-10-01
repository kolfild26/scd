package ru.sportmaster.scd.repository.adjustment.mapper;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.struct.TpDSCVal;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpDSCValMapper extends StructMapper<TpDSCVal> {
    public TpDSCValMapper() {
        super(TpDSCVal.class);
    }
}
