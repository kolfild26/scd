package ru.sportmaster.scd.repository.adjustment;

import ru.sportmaster.scd.entity.adjustment.AdjustmentDoc;

public interface AdjustmentDocRepository {
    AdjustmentDoc findById(Long documentId);

    Long create(Long typeId, String description, Long businessId, Long userId);

    void update(Long correctionId, String description, Long userId);

    void delete(Long correctionId, Long userId);

    void approve(Long correctionId, Long userId);

    void cancel(Long correctionId, Long userId);
}
