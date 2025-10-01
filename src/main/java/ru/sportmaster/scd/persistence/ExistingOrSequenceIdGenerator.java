package ru.sportmaster.scd.persistence;

import java.io.Serial;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

public class ExistingOrSequenceIdGenerator extends SequenceStyleGenerator {
    @Serial
    private static final long serialVersionUID = -8118506739081070940L;

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        Object id = session.getEntityPersister(null, object).getIdentifier(object, session);
        return id != null ? id : super.generate(session, object);
    }
}
