package ru.sportmaster.scd.struct.types;

import static java.util.Objects.isNull;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.SQLException;
import org.springframework.jdbc.core.SqlReturnType;
import ru.sportmaster.scd.struct.IStructMapper;

/**
 * Реализация интерфейса SqlReturnType, для удобного доступа к массивам структур, возвращаемых из хранимой процедуры.
 *
 * @param <T> - тип объектов java.
 */
@SuppressWarnings("unused")
public class SqlReturnStructArray<T> implements SqlReturnType {
    /**
     * Маппер объектов java в структуру оракл и обратно, коллекцию объектов java в массив структур оракл и обратно.
     */
    private final IStructMapper<T> mapper;

    public SqlReturnStructArray(IStructMapper<T> mapper) {
        this.mapper = mapper;
    }

    /**
     * Реализация для этого конкретного типа.
     * Этот метод вызывается внутренне платформой Spring Framework во время обработки параметров out,
     * и он не доступен непосредственно прикладному коду.
     */
    @Override
    public Object getTypeValue(CallableStatement cs, int paramIndex, int sqlType, String typeName) throws SQLException {
        Array array = (Array) cs.getObject(paramIndex);
        if (isNull(array)) {
            return null;
        } else {
            return mapper.toCollection(array, cs.getConnection()).toArray();
        }
    }
}
