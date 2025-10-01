package ru.sportmaster.scd.ui.view.annotation;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CalendarEditorType {
    WEEK("week"), DAY("day");

    @JsonValue
    private final String value;
}
