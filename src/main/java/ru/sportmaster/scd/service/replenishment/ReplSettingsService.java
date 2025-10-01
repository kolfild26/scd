package ru.sportmaster.scd.service.replenishment;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;

public interface ReplSettingsService {
    List<ObjectNode> getKgtAttrTreeValues(String parameter, Long id);
}
