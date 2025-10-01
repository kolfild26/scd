package ru.sportmaster.scd.entity.returnlogistic;

import lombok.Getter;

@Getter
public enum WareAccordanceLevel {
    Group("Товарная группа"),
    Category("Категория"),
    SubCategory("Подкатегория"),
    PlanningLine("Строка планирования");

    private final String value;

    WareAccordanceLevel(String value) {
        this.value = value;
    }
}
