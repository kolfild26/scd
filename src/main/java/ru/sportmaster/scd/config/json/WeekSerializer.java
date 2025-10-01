package ru.sportmaster.scd.config.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import ru.sportmaster.scd.entity.dictionary.Week;
import ru.sportmaster.scd.utils.UiUtil;

public class WeekSerializer extends JsonSerializer<Week> {
    @Override
    public void serialize(Week value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value != null && value.getName() != null) {
            gen.writeString(value.getName().format(UiUtil.DATE_FORMAT));
        }
    }
}
