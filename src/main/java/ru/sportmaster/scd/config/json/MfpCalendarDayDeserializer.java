package ru.sportmaster.scd.config.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.LocalDate;
import ru.sportmaster.scd.utils.UiUtil;

public class MfpCalendarDayDeserializer extends JsonDeserializer<Integer> {
    @Override
    public Integer deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        if (parser.getText() != null) {
            String value = LocalDate.parse(parser.getText(), UiUtil.DATE_FORMAT).format(UiUtil.DB_CALENDAR_ID_FORMAT);
            return Integer.parseInt(value);
        }
        return null;
    }
}
