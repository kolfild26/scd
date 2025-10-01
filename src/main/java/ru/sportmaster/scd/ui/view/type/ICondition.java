package ru.sportmaster.scd.ui.view.type;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ru.sportmaster.scd.config.json.ConditionDeserializer;

@JsonDeserialize(using = ConditionDeserializer.class)
public interface ICondition {
    boolean isGroup();
}
