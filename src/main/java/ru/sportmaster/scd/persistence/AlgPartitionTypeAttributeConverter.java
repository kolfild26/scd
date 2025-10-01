package ru.sportmaster.scd.persistence;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;
import ru.sportmaster.scd.entity.algorithms.definitions.AlgPartitionType;

@Slf4j
@Converter
public class AlgPartitionTypeAttributeConverter implements AttributeConverter<AlgPartitionType, Integer> {
    @Override
    public Integer convertToDatabaseColumn(AlgPartitionType attribute) {
        return attribute.ordinal();
    }

    @Override
    public AlgPartitionType convertToEntityAttribute(Integer dbData) {
        if (dbData >= AlgPartitionType.values().length) {
            log.warn("Неизвестный тип партиционирования: {}", dbData);
            return  AlgPartitionType.BY_ID_PARTITION_DIV_TMA;
        }
        return AlgPartitionType.values()[dbData];
    }
}
