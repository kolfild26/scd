package ru.sportmaster.scd.dictionary;

import static ru.sportmaster.scd.ui.view.dictionary.DictionaryType.ADJUST_DOCSTATUS;

import java.util.List;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.repository.adjustment.AdjustmentDocStatusRepository;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryItem;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryRegister;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryType;
import ru.sportmaster.scd.ui.view.type.LocalizedProperty;

@Component
@RequiredArgsConstructor
public class AdjustmentDocStatusRegister implements DictionaryRegister {
    private final AdjustmentDocStatusRepository adjustmentDocStatusRepository;

    @Override
    public DictionaryType getType() {
        return ADJUST_DOCSTATUS;
    }

    @Override
    public Supplier<List<DictionaryItem>> getItems() {
        return () -> adjustmentDocStatusRepository.findAll().stream()
            .map(type -> DictionaryItem.builder()
                .id(type.getId())
                .label(LocalizedProperty.of(type.getName()))
                .value(type.getId())
                .build())
            .toList();
    }
}
