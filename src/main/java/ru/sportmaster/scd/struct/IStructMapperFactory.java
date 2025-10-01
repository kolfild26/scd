package ru.sportmaster.scd.struct;

/**
 * Класс, управляющий созданием и подбором необходимых мапперов объектов Java в структуру Oracle и обратно,
 * коллекцию объектов Java в массив структур Oracle и обратно.
 */
public interface IStructMapperFactory {
    /**
     * Подбор или создание, при необходимости, маппера объектов Java в структуру Oracle и обратно,
     * коллекцию объектов Java в массив структур Oracle и обратно.
     *
     * @param mappedClass - класс маппируемых Java объектов.
     * @param <T>         - тип объектов Java.
     * @return - маппер объектов Java в структуру Oracle и обратно, коллекцию объектов Java в массив структур Oracle.
     */
    <T> IStructMapper<T> getStructMapper(Class<T> mappedClass);

    String getSchemaName();
}
