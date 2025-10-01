package ru.sportmaster.scd.repository.adjustment.mapper;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.struct.TpStopListStoreMSCS;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpStopListStoreMscsMapper extends StructMapper<TpStopListStoreMSCS> {
    public TpStopListStoreMscsMapper() {
        super(TpStopListStoreMSCS.class);
    }
}
