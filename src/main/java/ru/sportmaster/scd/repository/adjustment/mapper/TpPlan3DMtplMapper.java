package ru.sportmaster.scd.repository.adjustment.mapper;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.struct.TpPlan3DMtpl;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpPlan3DMtplMapper extends StructMapper<TpPlan3DMtpl> {
    public TpPlan3DMtplMapper() {
        super(TpPlan3DMtpl.class);
    }
}
