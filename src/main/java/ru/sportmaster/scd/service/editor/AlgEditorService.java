package ru.sportmaster.scd.service.editor;

import java.util.List;
import ru.sportmaster.scd.dto.adjustment.editor.AlgEditorSchemaDto;
import ru.sportmaster.scd.dto.adjustment.editor.CellDataDto;
import ru.sportmaster.scd.dto.adjustment.editor.CellType;
import ru.sportmaster.scd.web.UserEnvironment;

public interface AlgEditorService {
    List<AlgEditorSchemaDto> getSchemes();

    List<CellDataDto> getCellData(CellType type);

    AlgEditorSchemaDto saveSchema(AlgEditorSchemaDto schema, UserEnvironment environment);

    void deleteSchema(Long id);
}
