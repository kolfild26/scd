package ru.sportmaster.scd.mementity;

import java.util.Map;

public interface MemEntityRepository<K, T> {
    void reloadMap(Map<K, T> map);
}
