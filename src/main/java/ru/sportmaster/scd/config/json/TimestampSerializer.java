package ru.sportmaster.scd.config.json;

import static ru.sportmaster.scd.utils.UiUtil.DATE_TIME_FORMAT;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.ZoneId;

public class TimestampSerializer extends JsonSerializer<Timestamp> {
    private static final ZoneId SYSTEM_TIME_ZONE = ZoneId.systemDefault();
    private final ZoneId dbTimeZone;

    public TimestampSerializer(String dbTimeZonePrefix) {
        dbTimeZone = ZoneId.of(dbTimeZonePrefix);
    }

    @Override
    public void serialize(Timestamp value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value != null) {
            var dateTime = value.toLocalDateTime().atZone(SYSTEM_TIME_ZONE);
            gen.writeString(dateTime.withZoneSameInstant(dbTimeZone).format(DATE_TIME_FORMAT));
        }
    }
}
