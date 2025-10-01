package ru.sportmaster.scd.config.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.util.Optional;
import ru.sportmaster.scd.entity.returnlogistic.WareAccordanceLevel;

public class WareAccordanceLevelDeserializer extends JsonDeserializer<WareAccordanceLevel> {
    @Override
    public WareAccordanceLevel deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        return
            Optional.ofNullable(parser.getText())
                .map(WareAccordanceLevel::valueOf)
                .orElse(null);
    }
}
