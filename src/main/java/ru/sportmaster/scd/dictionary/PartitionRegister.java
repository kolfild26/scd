package ru.sportmaster.scd.dictionary;

import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.dictionary.Partition;
import ru.sportmaster.scd.repository.dictionary.PartitionRepository;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryItem;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryRegister;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryType;
import ru.sportmaster.scd.ui.view.type.LocalizedProperty;

@Component
@RequiredArgsConstructor
public class PartitionRegister implements DictionaryRegister {
    private final PartitionRepository partitionRepository;

    @Override
    public DictionaryType getType() {
        return DictionaryType.PARTITION;
    }

    @Override
    public Supplier<List<DictionaryItem>> getItems() {
        return () -> partitionRepository.findAll().stream()
            .sorted(Comparator.comparing(Partition::getId))
            .map(partition -> DictionaryItem.builder()
                .id(partition.getId())
                .value(partition.getId())
                .label(LocalizedProperty.of(partition.getDescription()))
                .build())
            .toList();
    }
}
