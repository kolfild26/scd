package ru.sportmaster.scd.dto.adjustment.editor;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CellDataDto {
    private String cuid;
    private String parentCuid;
    private CellType type;
    private JsonNode node;
    private List<CellDataDto> children;
}
