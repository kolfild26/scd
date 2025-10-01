package ru.sportmaster.scd.service.adjustment.io;

import java.util.List;
import ru.sportmaster.scd.entity.adjustment.AdjustmentType;

public interface AdjustmentDocumentWriter {
    byte[] buildTemplate(AdjustmentType type);

    byte[] buildDocument(AdjustmentType type, List<Object> rows);
}
