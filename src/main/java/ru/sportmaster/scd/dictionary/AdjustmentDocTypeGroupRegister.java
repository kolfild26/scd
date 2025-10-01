package ru.sportmaster.scd.dictionary;

import static ru.sportmaster.scd.ui.view.dictionary.DictionaryType.ADJUST_DOCTYPE_GROUP;

import java.util.List;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.repository.adjustment.AdjustmentDocTypeGroupRepository;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryItem;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryRegister;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryType;
import ru.sportmaster.scd.ui.view.type.LocalizedProperty;

@Component
@RequiredArgsConstructor
public class AdjustmentDocTypeGroupRegister implements DictionaryRegister {
    private final AdjustmentDocTypeGroupRepository adjustmentDocTypeGroupRepository;

    @Override
    public DictionaryType getType() {
        return ADJUST_DOCTYPE_GROUP;
    }

    @Override
    public Supplier<List<DictionaryItem>> getItems() {
        return () -> adjustmentDocTypeGroupRepository.findAll().stream()
            .map(type -> DictionaryItem.builder()
                .id(type.getId())
                .label(LocalizedProperty.of(type.getName()))
                .value(type.getId())
                .build())
            .toList();
    }
}
