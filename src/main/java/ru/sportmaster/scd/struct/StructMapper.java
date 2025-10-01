package ru.sportmaster.scd.struct;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import jakarta.persistence.Column;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Struct;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import oracle.jdbc.OracleArray;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleStruct;
import oracle.jdbc.OracleTypeMetaData;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import ru.sportmaster.scd.exceptions.ExecutingRuntimeException;
import ru.sportmaster.scd.struct.annotation.StructType;
import ru.sportmaster.scd.utils.ConvertUtil;

/**
 * Маппер объектов Java в структуру Oracle и обратно, коллекцию объектов Java в массив структур Oracle и обратно.
 *
 * @param <T> - тип объектов java.
 */
public class StructMapper<T> implements IStructMapper<T> {
    private static final String ARRAY_POSTFIX = "_LIST";
    private static final String CLOB_TYPE_NAME = "CLOB";
    private static final String BLOB_TYPE_NAME = "BLOB";

    @Getter
    private final Class<T> mappedClass;
    private final Map<String, PropertyDescriptor> mappedFields;
    private final String typeName;
    private final String arrayName;
    private String schemaName;
    private StructDefinition structDefinition = null;

    private IStructMapperFactory structMapperFactory = null;

    public StructMapper(Class<T> mappedClass) {
        this.mappedClass = Objects.requireNonNull(mappedClass);
        StructType structType = Optional.ofNullable(mappedClass.getAnnotation(StructType.class))
            .orElseThrow(() ->
                new DataRetrievalFailureException(
                    String.format("Маппируемый класс %s не помечен аннотацией StructType.",
                        mappedClass.getName())));
        this.mappedFields = initializeMappedFields();
        this.typeName = structType.value();
        this.arrayName = structType.arrayName();
    }

    @Override
    public void setStructMapperFactory(@NonNull IStructMapperFactory structMapperFactory) {
        this.structMapperFactory = structMapperFactory;
        this.schemaName = this.structMapperFactory.getSchemaName();
    }

    /**
     * Инициализация информации по полям объекта, представленной в виде Map с именем поля и его дескриптором.
     *
     * @return - Map с именами полей объекта и их дескрипторами.
     */
    private Map<String, PropertyDescriptor> initializeMappedFields() {
        Map<String, PropertyDescriptor> result = new HashMap<>();
        PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(this.mappedClass);
        for (PropertyDescriptor pd : pds) {
            if (nonNull(pd.getWriteMethod())) {
                Optional.ofNullable(getStructFieldName(pd.getName()))
                    .ifPresent(structFieldName -> result.put(structFieldName.toLowerCase(), pd));
            }
        }
        return result;
    }

    private String getStructFieldName(@NonNull String fieldName) {
        return Optional.ofNullable(getField(fieldName))
            .map(this::getNameByStructField)
            .orElse(null);
    }

    private String getNameByStructField(@NonNull Field field) {
        if (field.isAnnotationPresent(Column.class)) {
            String result = field.getAnnotation(Column.class).name();
            if (result.trim().isEmpty()) {
                result = field.getName();
            }
            return result.toLowerCase();
        }
        return null;
    }

    private Field getField(@NonNull String fieldName) {
        Field field = null;
        try {
            field = mappedClass.getField(fieldName);
        } catch (NoSuchFieldException exception) {
            try {
                field = mappedClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ignored) {
                //Ignored
            }
        }
        return field;
    }

    private StructDefinition getStructDefinition(Connection connection) {
        if (isNull(structDefinition)) {
            try {
                this.structDefinition = initializeStructDefinition(connection);
                this.structDefinition.checkMappedFields(mappedFields.keySet());
            } catch (SQLException e) {
                throw new ExecutingRuntimeException(e);
            }
        }
        return structDefinition;
    }

    private StructDefinition initializeStructDefinition(Connection connection) throws SQLException {
        OracleStruct oracleStruct = (OracleStruct) connection.createStruct(getFullTypeName(), null);
        OracleTypeMetaData metaData = oracleStruct.getOracleMetaData();
        OracleTypeMetaData.Struct typeStruct = (OracleTypeMetaData.Struct) metaData;
        return new StructDefinition(typeStruct.getMetaData(), getFullTypeName());
    }

    /**
     * Маппинг объекта Java в структуру Oracle.
     *
     * @param source - объекта Java типа, соответствующего мапперу.
     * @return - полученная структура Oracle.
     * @throws SQLException - выбрасывается исключение в случае возникновения ошибки БД.
     */
    @Override
    public Struct toStruct(T source, Connection connection) throws SQLException {
        var definition = getStructDefinition(connection);
        Object[] values = new Object[definition.getColumnsCount()];
        BeanWrapper beanWrapper = new BeanWrapperImpl(source);
        for (int index = 0; index < definition.getColumnsCount(); index++) {
            String column = definition.getColumnName(index);
            PropertyDescriptor fieldMeta = mappedFields.get(column);
            if (nonNull(fieldMeta)) {
                if (beanWrapper.isReadableProperty(fieldMeta.getName())) {
                    try {
                        values[index] =
                            Optional.ofNullable(
                                    getValueExchangePropertyType(
                                        fieldMeta,
                                        beanWrapper,
                                        definition.getColumnTypeName(index),
                                        connection)
                                )
                                .orElse(beanWrapper.getPropertyValue(fieldMeta.getName()));
                    } catch (NotReadablePropertyException ex) {
                        throw new DataRetrievalFailureException(
                            String.format("Unable to map column %s to property %s", column, fieldMeta.getName()),
                            ex);
                    }
                } else {
                    throw new DataRetrievalFailureException(
                        "Unable to access the getter for " + fieldMeta.getName()
                            + ".  Check that get " + StringUtils.capitalize(fieldMeta.getName())
                            + " is declared and has public access."
                    );
                }
            }
        }
        return connection.createStruct(getFullTypeName(), values);
    }

    /**
     * Получение значения из объекта, для полей "сложных" типов (коллекции, даты, UUID).
     *
     * @param fieldMeta      - дескриптор поля.
     * @param beanWrapper    - маппер объекта.
     * @param columnTypeName - имя колонки.
     * @return - значение из объекта.
     * @throws SQLException - выбрасывается исключение в случае возникновения ошибки БД.
     */
    private Object getValueExchangePropertyType(@NonNull PropertyDescriptor fieldMeta,
                                                @NonNull BeanWrapper beanWrapper,
                                                @NonNull String columnTypeName,
                                                Connection connection) throws SQLException {
        Class<?> propertyType = fieldMeta.getPropertyType();
        if (propertyType == List.class || propertyType == Set.class) {
            return fromArray(fieldMeta, beanWrapper, columnTypeName, connection);
        }
        if (propertyType == Date.class) {
            return Optional.ofNullable((Date) beanWrapper.getPropertyValue(fieldMeta.getName()))
                .map(Date::getTime)
                .map(Timestamp::new)
                .orElse(null);
        }
        if (propertyType == LocalDate.class) {
            return Optional.ofNullable((LocalDate) beanWrapper.getPropertyValue(fieldMeta.getName()))
                .map(LocalDate::atStartOfDay)
                .map(Timestamp::valueOf)
                .orElse(null);
        }
        if (propertyType == UUID.class) {
            var bytes = ConvertUtil.uuidToBytes((UUID) beanWrapper.getPropertyValue(fieldMeta.getName()));
            return bytes.length > 0 ? bytes : null;
        }
        if (propertyType == String.class && CLOB_TYPE_NAME.equalsIgnoreCase(columnTypeName)) {
            return ConvertUtil.stringToClob((String) beanWrapper.getPropertyValue(fieldMeta.getName()), connection);

        }
        if (propertyType == String.class && BLOB_TYPE_NAME.equalsIgnoreCase(columnTypeName)) {
            return ConvertUtil.stringToBlob((String) beanWrapper.getPropertyValue(fieldMeta.getName()), connection);

        }
        if (!BeanUtils.isSimpleValueType(propertyType)) {
            return getStruct(beanWrapper.getPropertyValue(fieldMeta.getName()), propertyType.getTypeName(), connection);
        }
        return null;
    }

    private Array fromArray(@NonNull PropertyDescriptor fieldMeta,
                            @NonNull BeanWrapper beanWrapper,
                            @NonNull String columnTypeName,
                            Connection connection) throws SQLException {
        Object[] obj = null;
        Class<?> propertyType = fieldMeta.getPropertyType();
        if (propertyType == List.class || propertyType == Set.class) {
            obj = Optional.ofNullable((Collection<?>) beanWrapper.getPropertyValue(fieldMeta.getName()))
                .map(Collection::toArray)
                .orElse(null);
        }
        return
            ((OracleConnection) connection)
                .createOracleArray(columnTypeName, buildArray(obj, getType(fieldMeta, beanWrapper), connection));
    }

    private Type getType(@NonNull PropertyDescriptor fieldMeta,
                         @NonNull BeanWrapper beanWrapper) {
        Field[] fields = beanWrapper
            .getWrappedClass()
            .getDeclaredFields();
        Field currentField = Stream.of(fields)
            .filter(field -> Objects.equals(field.getName().toLowerCase(), fieldMeta.getName().toLowerCase()))
            .findFirst()
            .orElseThrow(() -> new DataRetrievalFailureException("Unable map property " + fieldMeta.getName()));
        Type type = currentField.getGenericType();
        if (type instanceof ParameterizedType parameterizedType) {
            return parameterizedType.getActualTypeArguments()[0];
        }
        return type;
    }

    private Object[] buildArray(Object[] obj, Type type, Connection connection) {
        Object[] result = null;
        if (nonNull(obj)) {
            result = new Object[obj.length];
            int j = 0;
            if (BeanUtils.isSimpleValueType((Class<?>) type)) {
                result = obj;
            } else {
                for (Object o : obj) {
                    result[j] = getStruct(o, type.getTypeName(), connection);
                    ++j;
                }
            }
        }
        return result;
    }

    private Struct getStruct(Object obj, String className, Connection connection) {
        if (isNull(obj)) {
            return null;
        }
        try {
            return
                structMapperFactory.getStructMapper(Class.forName(className)).objectToStruct(obj, connection);
        } catch (Exception e) {
            throw new DataRetrievalFailureException(
                String.format("Ошибка получения StructMapper для класса %s", className),
                e);
        }
    }

    @Override
    public Struct objectToStruct(Object source, Connection connection) throws SQLException {
        return toStruct(mappedClass.cast(source), connection);
    }

    /**
     * Маппинг структуры Oracle в объекта Java.
     *
     * @param struct - структура Oracle.
     * @return - объект Java типа, соответствующего мапперу.
     * @throws SQLException - выбрасывается исключение в случае возникновения ошибки БД.
     */
    @Override
    public T fromStruct(Struct struct, Connection connection) throws SQLException {
        T mappedObject = BeanUtils.instantiateClass(mappedClass);
        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(mappedObject);
        Object[] attr = struct.getAttributes();
        var definition = getStructDefinition(connection);
        for (int index = 0; index < definition.getColumnsCount(); index++) {
            String column = definition.getColumnName(index);
            PropertyDescriptor propertyDescriptor = mappedFields.get(column);
            if (nonNull(propertyDescriptor)) {
                try {
                    beanWrapper.setPropertyValue(
                        propertyDescriptor.getName(),
                        getValue(attr[index], beanWrapper, propertyDescriptor, connection));
                } catch (NotWritablePropertyException ex) {
                    throw new DataRetrievalFailureException(
                        String.format("Unable to map column %s to property %s", column, propertyDescriptor.getName()),
                        ex
                    );
                }
            }
        }
        return mappedObject;
    }

    private Object getValue(Object value,
                            @NonNull BeanWrapper beanWrapper,
                            @NonNull PropertyDescriptor propertyDescriptor,
                            Connection connection) throws SQLException {
        if (isNull(value)) {
            return null;
        }
        if (value instanceof Struct struct) {
            Type type = getType(propertyDescriptor, beanWrapper);
            return structMapperFactory.getStructMapper((Class<?>) type).fromStruct(struct, connection);
        }
        if (value instanceof OracleArray oracleArray) {
            Type type = getType(propertyDescriptor, beanWrapper);
            return toArray(oracleArray, type, connection);
        }
        if (propertyDescriptor.getPropertyType() == UUID.class && value instanceof byte[] bytes) {
            return ConvertUtil.bytesToUuid(bytes);
        }
        if (value instanceof Clob clob && propertyDescriptor.getPropertyType() == String.class) {
            return ConvertUtil.clobToString(clob);
        }
        if (value instanceof Blob blob && propertyDescriptor.getPropertyType() == String.class) {
            try {
                return ConvertUtil.getBlobString(blob);
            } catch (Exception e) {
                throw new InvalidDataAccessApiUsageException("Cannot convert BLOB to String", e);
            }
        }
        return value;
    }

    private List<Object> toArray(OracleArray array, Type type, Connection connection) throws SQLException {
        Object[] arrayValues = (Object[]) array.getArray();
        List<Object> values = new ArrayList<>();
        for (Object arrayValue : arrayValues) {
            values.add(getFromArrayValue(arrayValue, type, connection));
        }
        return values;
    }

    /**
     * Маппинг коллекции объектов Java в массив структур Oracle.
     *
     * @param sources - коллекция объектов Java типа, соответствующего мапперу.
     * @return - массив структур Oracle.
     * @throws SQLException - выбрасывается исключение в случае возникновения ошибки БД.
     */
    @Override
    public Array toArray(Collection<T> sources, Connection connection) throws SQLException {
        if (CollectionUtils.isEmpty(sources)) {
            return null;
        }
        return
            ((OracleConnection) connection)
                .createOracleArray(
                    getFullArrayName(),
                    sources.stream()
                        .map(object -> {
                            try {
                                return toStruct(object, connection);
                            } catch (SQLException e) {
                                throw new InvalidDataAccessApiUsageException(
                                    String.format("Error convert %s to struct: %s",
                                        object, e.getMessage()),
                                    e);
                            }
                        })
                        .filter(Objects::nonNull)
                        .toArray());
    }

    private Object getFromArrayValue(Object struct, Type type, Connection connection) throws SQLException {
        if (isNull(struct)) {
            throw new InvalidDataAccessApiUsageException("Expected STRUCT but got 'null'");
        }
        if (struct instanceof Struct structure) {
            return structMapperFactory.getStructMapper((Class<?>) type).fromStruct(structure, connection);
        }
        if (type == UUID.class) {
            return ConvertUtil.bytesToUuid((byte[]) struct);
        }
        if (struct instanceof Clob clob && type == String.class) {
            return ConvertUtil.clobToString(clob);
        }
        if (struct instanceof Blob blob && type == String.class) {
            try {
                return ConvertUtil.getBlobString(blob);
            } catch (Exception e) {
                throw new InvalidDataAccessApiUsageException("Cannot convert BLOB to String", e);
            }
        }
        return struct;
    }

    @Override
    public String getFullTypeName() {
        return getFullName(typeName);
    }

    /**
     * Получить полное имя типа массива Oracle (вместе с именем схемы).
     *
     * @return - полное имя типа массива Oracle.
     */
    @Override
    public String getFullArrayName() {
        return isNull(arrayName) || arrayName.trim().isEmpty()
            ? getFullTypeName() + ARRAY_POSTFIX
            : getFullName(arrayName);
    }

    /**
     * Получить полное имя типа Oracle (вместе с именем схемы).
     *
     * @param name - заданное имя типа Oracle.
     * @return - полное имя типа Oracle.
     */
    @Override
    public String getFullName(String name) {
        return isNull(schemaName) || schemaName.trim().isEmpty()
            ? name
            : schemaName + "." + name;
    }

    /**
     * Маппинг массив структур Oracle в коллекцию объектов Java.
     *
     * @param array - массив структур Oracle.
     * @return - коллекция объектов Java типа, соответствующего мапперу.
     * @throws SQLException - выбрасывается исключение в случае возникновения ошибки БД.
     */
    @SuppressWarnings("unused")
    @Override
    public Collection<T> toCollection(Array array, Connection connection) throws SQLException {
        Object[] structValues = (Object[]) array.getArray();
        List<T> values = new ArrayList<>();
        for (Object struct : structValues) {
            if (!(struct instanceof Struct)) {
                if (isNull(struct)) {
                    throw new InvalidDataAccessApiUsageException("Expected STRUCT but got 'null'");
                } else {
                    throw new InvalidDataAccessApiUsageException(
                        "Expected STRUCT but got " + struct.getClass().getName());
                }
            }
            values.add(fromStruct((Struct) struct, connection));
        }
        return values;
    }

    /**
     * Маппинг массива неприведенных объектов к списку типизированных объектов.
     *
     * @param objects - массив неприведенных объектов.
     * @return - список типизированных объектов.
     */
    @SuppressWarnings("unused")
    @Override
    public List<T> toList(Object... objects) {
        return toList(mappedClass::cast, objects);
    }

    /**
     * Маппинг массива неприведенных объектов к списку типизированных объектов, с использованием маппера.
     *
     * @param objects - массив неприведенных объектов.
     * @param mapper  - функция приведения Object в необходимый тип.
     * @return - список типизированных объектов.
     */
    @Override
    public <E> List<E> toList(Function<? super Object, E> mapper,
                              Object... objects) {
        if (isNull(objects)) {
            return Collections.emptyList();
        }
        return Stream.of(objects)
            .map(mapper)
            .toList();
    }

    private static class StructDefinition {
        private final List<Pair<String, String>> columnsDefinition = new ArrayList<>();
        private final String fullTypeName;

        StructDefinition(ResultSetMetaData resultSetMetaData,
                         String fullTypeName) throws SQLException {
            this.fullTypeName = fullTypeName;
            if (isNull(resultSetMetaData) || resultSetMetaData.getColumnCount() == 0) {
                throw new DataRetrievalFailureException(
                    String.format("Unable get oracle struct %s.", this.fullTypeName));
            }
            for (int index = 1; index <= resultSetMetaData.getColumnCount(); index++) {
                columnsDefinition.add(
                    Pair.of(
                        JdbcUtils.lookupColumnName(resultSetMetaData, index).toLowerCase(),
                        resultSetMetaData.getColumnTypeName(index)
                    )
                );
            }
        }

        int getColumnsCount() {
            return columnsDefinition.size();
        }

        String getColumnName(int index) {
            return columnsDefinition.get(index).getLeft();
        }

        String getColumnTypeName(int index) {
            return columnsDefinition.get(index).getRight();
        }

        void checkMappedFields(@NonNull Set<String> mappedFields) {
            Set<String> structFieldNames = columnsDefinition.stream().map(Pair::getLeft).collect(Collectors.toSet());
            if (!structFieldNames.containsAll(mappedFields)) {
                throw new DataRetrievalFailureException(
                    String.format("Check oracle struct %s error.", fullTypeName));
            }
        }
    }
}
