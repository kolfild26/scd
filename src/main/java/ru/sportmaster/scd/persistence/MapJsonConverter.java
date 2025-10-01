package ru.sportmaster.scd.persistence;

import static java.util.Objects.nonNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Converter
public class MapJsonConverter implements AttributeConverter<Map<String, Object>, byte[]> {
    private final ObjectMapper objectMapper;

    public MapJsonConverter() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public byte[] convertToDatabaseColumn(Map<String, Object> mapData) {
        byte[] result = null;
        if (nonNull(mapData) && !mapData.isEmpty()) {
            try {
                result = objectMapper.writeValueAsBytes(mapData);
            } catch (final JsonProcessingException e) {
                log.error("JSON writing error", e);
            }
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> convertToEntityAttribute(byte[] valueJSON) {
        Map<String, Object> result = null;
        if (nonNull(valueJSON) && valueJSON.length != 0) {
            try {
                result = objectMapper.readValue(valueJSON, Map.class);
            } catch (final IOException e) {
                log.error("JSON reading error", e);
            }
        }
        return result;
    }
}
