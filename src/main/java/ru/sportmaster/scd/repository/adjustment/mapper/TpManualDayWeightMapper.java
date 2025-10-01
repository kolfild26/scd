package ru.sportmaster.scd.repository.adjustment.mapper;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.struct.TpManualDayWeight;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpManualDayWeightMapper extends StructMapper<TpManualDayWeight> {
    public TpManualDayWeightMapper() {
        super(TpManualDayWeight.class);
    }
}
