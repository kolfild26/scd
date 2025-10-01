package ru.sportmaster.scd.repository.adjustment.mapper;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.struct.TpStartListStoreDSCS;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpStartListStoreDscsMapper extends StructMapper<TpStartListStoreDSCS> {
    public TpStartListStoreDscsMapper() {
        super(TpStartListStoreDSCS.class);
    }
}
