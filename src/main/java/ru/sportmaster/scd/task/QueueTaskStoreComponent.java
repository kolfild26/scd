package ru.sportmaster.scd.task;

import static java.util.Objects.nonNull;

import com.hazelcast.map.MapStore;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.repository.algorithms.QueueTaskStoreRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class QueueTaskStoreComponent implements MapStore<AlgorithmTask, AlgorithmTaskState> {
    private final QueueTaskStoreRepository queueTaskStoreRepository;

    @Override
    public void delete(AlgorithmTask key) {
        if (nonNull(key)) {
            queueTaskStoreRepository.deleteById(key.getAlgorithmId());
            log.debug("Task queue item deleted {}.", key);
        } else {
            log.debug("Task queue item is NULL.");
        }
    }

    @Override
    public void deleteAll(Collection<AlgorithmTask> keys) {
        if (nonNull(keys) && !keys.isEmpty()) {
            keys.forEach(this::delete);
            log.debug("Task queue items deleted all.");
        } else {
            log.debug("Task queue items is empty.");
        }
    }

    @Override
    public void store(AlgorithmTask key, AlgorithmTaskState value) {
        if (nonNull(key) && nonNull(value)) {
            queueTaskStoreRepository.merge(
                queueTaskStoreRepository.create(key, value)
            );
            log.debug("Task queue item stored {}.", key);
        } else {
            log.debug("Task queue item is null.");
        }
    }

    @Override
    public void storeAll(Map<AlgorithmTask, AlgorithmTaskState> map) {
        if (nonNull(map) && !map.isEmpty()) {
            map.forEach(this::store);
            log.debug("Task queue items stored all.");
        } else {
            log.debug("Task queue items is empty.");
        }
    }

    @Override
    public AlgorithmTaskState load(AlgorithmTask key) {
        log.debug("Task queue item load {}.", key);
        return queueTaskStoreRepository.load(key);
    }

    @Override
    public Map<AlgorithmTask, AlgorithmTaskState> loadAll(Collection<AlgorithmTask> keys) {
        log.debug("Task queue items load aLl.");
        return queueTaskStoreRepository.loadAll(keys);
    }

    @Override
    public Set<AlgorithmTask> loadAllKeys() {
        log.debug("Task queue items load all keys.");
        return queueTaskStoreRepository.loadAllKeys();
    }
}
