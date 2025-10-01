package ru.sportmaster.scd.service.adjustment.io;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import ru.sportmaster.scd.entity.adjustment.AdjustmentType;

public interface AdjustmentDocumentReader {
    List<ObjectNode> convert(AdjustmentType type, MultipartFile file);

    List<ObjectNode> convertToEntities(AdjustmentType type, List<ObjectNode> nodes);
}
