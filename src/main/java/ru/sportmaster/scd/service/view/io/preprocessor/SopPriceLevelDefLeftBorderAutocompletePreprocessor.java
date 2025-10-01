package ru.sportmaster.scd.service.view.io.preprocessor;

import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.simple3dplan.SopPriceLevelDef;
import ru.sportmaster.scd.entity.simple3dplan.SopPriceLevelDefKey_;
import ru.sportmaster.scd.entity.simple3dplan.SopPriceLevelDef_;

@Component
@RequiredArgsConstructor
public class SopPriceLevelDefLeftBorderAutocompletePreprocessor implements ViewFileRequestBuilderPreprocessor {
    private static final Map<String, Map<String, Integer>> SOP_PRISE_BORDERS_MAP = new ConcurrentHashMap<>();
    private static final Map<String, List<String>> AUTOCOMPLETED_IDS = new ConcurrentHashMap<>();

    @Override
    public Class<?> getTargetClass() {
        return SopPriceLevelDef.class;
    }

    @Override
    public void apply(String uuid, ObjectNode node) {
        var bordersMap = SOP_PRISE_BORDERS_MAP.computeIfAbsent(uuid, key -> new HashMap<>());
        var autoCompletedIds = AUTOCOMPLETED_IDS.computeIfAbsent(uuid, key -> new ArrayList<>());

        var id = getSopPriceLevelDefGroupId(node);
        var rightBorder = node.get(SopPriceLevelDef_.RIGHT_BORDER).asInt();

        if (bordersMap.containsKey(id)) {
            var prevRightBorder = bordersMap.get(id) + 1;
            if (prevRightBorder != rightBorder) {
                node.put(SopPriceLevelDef_.LEFT_BORDER, prevRightBorder);
                bordersMap.replace(id, rightBorder);
                autoCompletedIds.add(id);
            }
        } else {
            bordersMap.put(id, rightBorder);
        }
    }

    @Override
    public boolean validate(String uuid, ObjectNode node) {
        var rightBorder = node.get(SopPriceLevelDef_.RIGHT_BORDER);
        var leftBorder = node.get(SopPriceLevelDef_.LEFT_BORDER);

        if (isNull(rightBorder) || isNull(leftBorder)) {
            return false;
        }

        if (rightBorder.isNull() || leftBorder.isNull()) {
            return false;
        }

        return rightBorder.asInt() > leftBorder.asInt();
    }

    @Override
    public boolean isEditable(String uuid, ObjectNode node) {
        var id = getSopPriceLevelDefGroupId(node);
        return ofNullable(AUTOCOMPLETED_IDS.get(uuid)).map(ids -> ids.contains(id)).orElse(false);
    }

    @SneakyThrows
    private String getSopPriceLevelDefGroupId(ObjectNode node) {
        ObjectNode id = node.get(SopPriceLevelDef_.ID).deepCopy();
        id.remove(SopPriceLevelDefKey_.PRICE_LEVEL_ID);

        return id.toString();
    }

    @Override
    public void clear(String uuid) {
        SOP_PRISE_BORDERS_MAP.remove(uuid);
        AUTOCOMPLETED_IDS.remove(uuid);
    }
}
