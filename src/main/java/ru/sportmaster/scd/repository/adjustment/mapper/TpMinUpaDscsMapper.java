package ru.sportmaster.scd.repository.adjustment.mapper;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.struct.TpMinUpaDSCS;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpMinUpaDscsMapper extends StructMapper<TpMinUpaDSCS> {
    public TpMinUpaDscsMapper() {
        super(TpMinUpaDSCS.class);
    }
}
