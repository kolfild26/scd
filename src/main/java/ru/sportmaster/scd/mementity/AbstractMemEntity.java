package ru.sportmaster.scd.mementity;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AbstractMemEntity<K, T> implements IMemEntity<K, T> {
    protected final String mapName;
    protected final MemEntityRepository<K, T> repository;

    @Getter
    protected final String name;

    @Getter
    protected final Long reloadPeriod;

    @Getter
    private final Map<K, T> map = new HashMap<>();

    public AbstractMemEntity(String mapName,
                             MemEntityRepository<K, T> repository,
                             String name,
                             Long reloadPeriod) {
        this.mapName = mapName;
        this.repository = repository;
        this.name = name;
        this.reloadPeriod = reloadPeriod;
    }

    @Override
    public void reload() {
        repository.reloadMap(map);
    }
}
