package ru.sportmaster.scd.persistence;

import static java.util.Objects.isNull;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import ru.sportmaster.scd.utils.CollectionUtil;

@Converter
public class ListLongConverter implements AttributeConverter<List<Long>, String> {
    @Override
    public String convertToDatabaseColumn(List<Long> list) {
        return isNull(list) ? null : CollectionUtil.join(list);
    }

    @Override
    public List<Long> convertToEntityAttribute(String str) {
        if (isNull(str) || str.isBlank()) {
            return Collections.emptyList();
        }
        String[] value = str.replace("[", "").replace("]", "").split(",");
        return Arrays.stream(value).map(String::trim).filter(i -> !i.isBlank()).map(Long::parseLong).toList();
    }
}
