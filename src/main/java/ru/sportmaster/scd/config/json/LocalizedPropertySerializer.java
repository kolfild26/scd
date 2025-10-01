package ru.sportmaster.scd.config.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import ru.sportmaster.scd.ui.view.type.LocalizedProperty;

@AllArgsConstructor
public class LocalizedPropertySerializer extends JsonSerializer<LocalizedProperty> {
    private final MessageSource messageSource;

    @Override
    public void serialize(LocalizedProperty property, JsonGenerator gen, SerializerProvider serializers)
        throws IOException {
        String localized = null;
        for (String key : property.getMessageKeys()) {
            localized = messageSource.getMessage(key, null, null, LocaleContextHolder.getLocale());
            if (localized != null) {
                break;
            }
        }

        gen.writeString(localized != null ? localized : property.getLastKey());
    }
}
