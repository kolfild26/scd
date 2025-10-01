package ru.sportmaster.scd.dto.settings;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlgType {
    REPL_STRATEGY(3L),
    FORECAST(4L),
    PLAN_LIMIT(5L),
    ALLOCATION(8L);

    private final Long algorithmDefId;
}
