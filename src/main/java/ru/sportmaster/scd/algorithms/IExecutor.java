package ru.sportmaster.scd.algorithms;

import java.util.Map;

public interface IExecutor {
    /**
     * Согласованное имя исполнителя.
     * @return - имя исполнителя.
     */
    String getName();

    /**
     * Описание исполнителя. Краткое изложение сути проводимых операций.
     * @return - описание исполнителя.
     */
    String getDescription();

    /**
     * Выполнение.
     * @param params - входящие параметры.
     * @return - параметры, доопределенные на этапе выполнения, и результат выполнения.
     */
    Map<String, Object> execute(Map<String, Object> params);
}
