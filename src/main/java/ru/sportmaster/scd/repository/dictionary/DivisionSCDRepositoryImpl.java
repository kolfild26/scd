package ru.sportmaster.scd.repository.dictionary;

import org.springframework.stereotype.Repository;
import ru.sportmaster.scd.entity.dictionary.DivisionSCD;
import ru.sportmaster.scd.repository.AbstractRepositoryImpl;

@Repository
public class DivisionSCDRepositoryImpl
    extends AbstractRepositoryImpl<DivisionSCD, Long>
    implements DivisionSCDRepository {
    public DivisionSCDRepositoryImpl() {
        super(DivisionSCD.class);
    }
}
