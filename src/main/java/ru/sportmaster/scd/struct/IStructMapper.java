package ru.sportmaster.scd.struct;

import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import org.springframework.lang.NonNull;

@SuppressWarnings("unused")
public interface IStructMapper<T> {
    Class<?> getMappedClass();

    String getFullTypeName();

    String getFullName(String name);

    String getFullArrayName();

    Struct toStruct(T source, Connection connection) throws SQLException;

    Struct objectToStruct(Object source, Connection connection) throws SQLException;

    T fromStruct(Struct struct, Connection connection) throws SQLException;

    Array toArray(Collection<T> sources, Connection connection) throws SQLException;

    Collection<T> toCollection(Array array, Connection connection) throws SQLException;

    List<T> toList(Object... objects);

    <E> List<E> toList(Function<? super Object, E> mapper, Object... objects);

    void setStructMapperFactory(@NonNull IStructMapperFactory structMapperFactory);
}
