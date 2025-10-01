package ru.sportmaster.scd.config.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.LocalDate;
import ru.sportmaster.scd.utils.UiUtil;

public class MfpCalendarDaySerializer extends JsonSerializer<Integer> {
    @Override
    public void serialize(Integer value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value != null) {
            gen.writeString(LocalDate.parse(value.toString(), UiUtil.DB_CALENDAR_ID_FORMAT).format(UiUtil.DATE_FORMAT));
        }
    }
}
