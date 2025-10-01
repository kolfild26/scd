package ru.sportmaster.scd.persistence;

import static java.util.Objects.isNull;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.stream.Stream;
import ru.sportmaster.scd.entity.returnlogistic.WareAccordanceLevel;

@Converter
public class WareAccordanceLevelAttributeConverter implements AttributeConverter<WareAccordanceLevel, String> {
    @Override
    public String convertToDatabaseColumn(WareAccordanceLevel wareAccordanceLevel) {
        return isNull(wareAccordanceLevel) ? null : wareAccordanceLevel.getValue();
    }

    @Override
    public WareAccordanceLevel convertToEntityAttribute(String str) {
        if (isNull(str) || str.isBlank()) {
            return null;
        }

        return
            Stream.of(WareAccordanceLevel.values())
                .filter(enumValue -> str.equals(enumValue.getValue()))
                .findFirst()
                .orElse(null);
    }
}
