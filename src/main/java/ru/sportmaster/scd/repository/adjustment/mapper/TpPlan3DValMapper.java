package ru.sportmaster.scd.repository.adjustment.mapper;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.struct.TpPlan3DVal;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpPlan3DValMapper extends StructMapper<TpPlan3DVal> {
    public TpPlan3DValMapper() {
        super(TpPlan3DVal.class);
    }
}
