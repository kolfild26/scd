package ru.sportmaster.scd.repository.adjustment;

import java.util.List;
import ru.sportmaster.scd.entity.adjustment.struct.ITpAdjustmentRow;

public interface AdjustmentDocRowRepository {
    List<Object> getAllRowsByDocumentId(Class<?> rowType, Long correctionId);

    boolean hasRows(Class<?> rowType, Long correctionId);

    void create(Long correctionId, ITpAdjustmentRow row);

    Long create(Long typeId, String description, Long businessId, Long userId, List<ITpAdjustmentRow> rows);

    void update(Long correctionId, ITpAdjustmentRow row);

    Long update(Long correctionId, List<ITpAdjustmentRow> rows);

    void delete(Long correctionId, ITpAdjustmentRow row);
}
