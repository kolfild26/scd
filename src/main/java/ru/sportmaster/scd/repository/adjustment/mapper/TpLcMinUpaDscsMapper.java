package ru.sportmaster.scd.repository.adjustment.mapper;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.struct.TpLcMinUpaDSCS;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpLcMinUpaDscsMapper extends StructMapper<TpLcMinUpaDSCS> {
    public TpLcMinUpaDscsMapper() {
        super(TpLcMinUpaDSCS.class);
    }
}
