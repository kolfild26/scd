package ru.sportmaster.scd.algorithms;

import java.time.LocalDateTime;
import java.util.Map;

public interface IAlgComponent {
    Long getId();

    IAlgComponentDefine getDefine();

    LocalDateTime getStartTime();

    LocalDateTime getEndTime();

    LocalDateTime getCancelTime();

    Map<String, Object> getResult();
}
