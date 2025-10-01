package ru.sportmaster.scd.dto.adjustment.editor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlgEditorSchemaDto {
    private Long id;
    private String name;
    private String description;
    private Map<String, Object> defaultParams;
    private List<CellDataDto> cellData;
    private Map<ChangeType, List<CellDataDto>> changes;

    @JsonIgnore
    public List<CellDataDto> getCreateAndUpdateChanges() {
        List<CellDataDto> result = new ArrayList<>();
        if (this.hasChanges()) {
            result.addAll(changes.get(ChangeType.CREATE));
            result.addAll(changes.get(ChangeType.UPDATE));
        }
        return result;
    }

    @JsonIgnore
    public List<CellDataDto> getDeleteChanges() {
        if (this.hasChanges()) {
            return changes.get(ChangeType.DELETE);
        }
        return Collections.emptyList();
    }

    @JsonIgnore
    public boolean hasChanges() {
        return changes != null && !changes.isEmpty();
    }
}
