package ru.sportmaster.scd.repository.adjustment.mapper;

import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.struct.TpStoreOpenStoreCloseDate;
import ru.sportmaster.scd.struct.StructMapper;

@Component
public class TpStoreOpenStoreCloseDateMapper extends StructMapper<TpStoreOpenStoreCloseDate> {
    public TpStoreOpenStoreCloseDateMapper() {
        super(TpStoreOpenStoreCloseDate.class);
    }
}
