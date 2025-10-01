package ru.sportmaster.scd.entity.simple3dplan;

import lombok.Getter;

@Getter
public enum DistrSource {
    FACT("Факт LY"),
    PLAN("План LY");

    private final String value;

    DistrSource(String value) {
        this.value = value;
    }
}
