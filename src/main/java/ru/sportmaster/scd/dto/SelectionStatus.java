package ru.sportmaster.scd.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Schema(enumAsRef = true, description = """
    Статус выбора:
        
    `SELECT_ALL` - Выбрано все,
    `SELECT_SEVERAL` - Выбрано несколько,
    `SELECT_NONE` - Ничего не выбрано
    """)
public enum SelectionStatus {
    SELECT_ALL(true), SELECT_SEVERAL(false), SELECT_NONE(null);

    private final Boolean value;

    @JsonValue
    public Boolean getValue() {
        return value;
    }
}
