package ru.sportmaster.scd.repository.adjustment.mapper;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.struct.TpLcMinUpaMSCS;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpLcMinUpaMscsMapper extends StructMapper<TpLcMinUpaMSCS> {
    public TpLcMinUpaMscsMapper() {
        super(TpLcMinUpaMSCS.class);
    }
}
