package ru.sportmaster.scd.mementity;

import org.springframework.lang.NonNull;

public interface MemEntityCheckProcessorRepository<K> {
    void saveNum(@NonNull String formUuid,
                 Long userId,
                 MemEntitySelection<K> selection);

    void close(@NonNull String formUuid);
}
