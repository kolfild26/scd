package ru.sportmaster.scd.persistence;

import static java.util.Objects.isNull;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.stream.Stream;
import ru.sportmaster.scd.entity.simple3dplan.DistrSource;

@Converter
public class DistrSourceAttributeConverter implements AttributeConverter<DistrSource, String> {
    @Override
    public String convertToDatabaseColumn(DistrSource distrSource) {
        return isNull(distrSource) ? null : distrSource.getValue();
    }

    @Override
    public DistrSource convertToEntityAttribute(String str) {
        if (isNull(str) || str.isBlank()) {
            return null;
        }
        return
            Stream.of(DistrSource.values())
                .filter(enumValue -> str.equals(enumValue.getValue()))
                .findFirst()
                .orElse(null);
    }
}
