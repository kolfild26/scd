package ru.sportmaster.scd.repository.adjustment.mapper;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.struct.TpStartListStoreMSCS;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpStartListStoreMscsMapper extends StructMapper<TpStartListStoreMSCS> {
    public TpStartListStoreMscsMapper() {
        super(TpStartListStoreMSCS.class);
    }
}
