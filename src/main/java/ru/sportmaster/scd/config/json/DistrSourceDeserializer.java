package ru.sportmaster.scd.config.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.util.Optional;
import ru.sportmaster.scd.entity.simple3dplan.DistrSource;

public class DistrSourceDeserializer extends JsonDeserializer<DistrSource> {
    @Override
    public DistrSource deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        return
            Optional.ofNullable(parser.getText())
                .map(DistrSource::valueOf)
                .orElse(null);
    }
}
