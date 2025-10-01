package ru.sportmaster.scd.config.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import ru.sportmaster.scd.ui.view.type.LocalizedProperty;

public class LocalizedPropertyDeserializer extends JsonDeserializer<LocalizedProperty> {
    @Override
    public LocalizedProperty deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        return new LocalizedProperty(parser.getText());
    }
}
