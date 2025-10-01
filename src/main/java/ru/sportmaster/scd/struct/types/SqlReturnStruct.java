package ru.sportmaster.scd.struct.types;

import static java.util.Objects.isNull;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Struct;
import org.springframework.jdbc.core.SqlReturnType;
import ru.sportmaster.scd.struct.IStructMapper;

/**
 * Реализация интерфейса SqlReturnType, для удобства доступа к объектным данным, возвращаемым из хранимой процедуры.
 */
@SuppressWarnings("unused")
public class SqlReturnStruct implements SqlReturnType {
    /**
     * Маппер объектов java в структуру оракл и обратно, коллекцию объектов java в массив структур оракл и обратно.
     */
    private final IStructMapper<?> mapper;

    public SqlReturnStruct(IStructMapper<?> mapper) {
        this.mapper = mapper;
    }

    /**
     * Реализация для этого конкретного типа.
     * Этот метод вызывается внутренне платформой Spring Framework во время обработки параметров out,
     * и он не доступен непосредственно прикладному коду.
     */
    @Override
    public Object getTypeValue(CallableStatement cs, int paramIndex, int sqlType, String typeName) throws SQLException {
        Struct struct = (Struct) cs.getObject(paramIndex);
        if (isNull(struct)) {
            return null;
        } else {
            return this.mapper.fromStruct(struct, cs.getConnection());
        }
    }
}
