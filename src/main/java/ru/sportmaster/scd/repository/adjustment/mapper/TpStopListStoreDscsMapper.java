package ru.sportmaster.scd.repository.adjustment.mapper;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.struct.TpStopListStoreDSCS;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpStopListStoreDscsMapper extends StructMapper<TpStopListStoreDSCS> {
    public TpStopListStoreDscsMapper() {
        super(TpStopListStoreDSCS.class);
    }
}
