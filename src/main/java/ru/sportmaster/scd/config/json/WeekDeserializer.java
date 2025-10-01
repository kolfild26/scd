package ru.sportmaster.scd.config.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.LocalDate;
import ru.sportmaster.scd.entity.dictionary.Week;
import ru.sportmaster.scd.utils.UiUtil;

public class WeekDeserializer extends JsonDeserializer<Week> {
    @Override
    public Week deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        if (parser.getText() != null) {
            String value = LocalDate.parse(parser.getText(), UiUtil.DATE_FORMAT).format(UiUtil.DB_CALENDAR_ID_FORMAT);
            return Week.builder().id(Long.parseLong(value)).build();
        }
        return null;
    }
}
