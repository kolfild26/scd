package ru.sportmaster.scd.mementity;

import java.util.Map;

public interface IMemEntity<K, T> {
    String getName();

    void reload();

    Long getReloadPeriod();

    Map<K, T> getMap();
}
