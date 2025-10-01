package ru.sportmaster.scd.repository.adjustment.mapper;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.struct.TpMinUpaMSCS;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpMinUpaMscsMapper extends StructMapper<TpMinUpaMSCS> {
    public TpMinUpaMscsMapper() {
        super(TpMinUpaMSCS.class);
    }
}
