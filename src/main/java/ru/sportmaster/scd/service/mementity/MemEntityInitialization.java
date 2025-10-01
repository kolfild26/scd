package ru.sportmaster.scd.service.mementity;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.sportmaster.scd.mementity.IMemEntity;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemEntityInitialization {
    private final List<IMemEntity<?, ?>> memEntities;

    public void init() {
        memEntities.forEach(memEntity ->
            Executors
                .newSingleThreadScheduledExecutor()
                .schedule(() -> initMemEntity(memEntity), 10, TimeUnit.MILLISECONDS)
        );
    }

    private void initMemEntity(IMemEntity<?, ?> memEntity) {
        var executorService = Executors.newSingleThreadScheduledExecutor();
        log.debug(
            "{} инициализация перезагрузки данных на каждые {} минут.",
            memEntity.getName(), memEntity.getReloadPeriod()
        );
        executorService.scheduleAtFixedRate(
            memEntity::reload,
            0,
            memEntity.getReloadPeriod(),
            TimeUnit.MINUTES
        );
    }
}
