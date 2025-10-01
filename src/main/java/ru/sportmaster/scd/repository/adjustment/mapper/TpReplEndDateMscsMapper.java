package ru.sportmaster.scd.repository.adjustment.mapper;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.struct.TpReplEndDateMSCS;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpReplEndDateMscsMapper extends StructMapper<TpReplEndDateMSCS> {
    public TpReplEndDateMscsMapper() {
        super(TpReplEndDateMSCS.class);
    }
}
