package ru.sportmaster.scd.dictionary;

import static ru.sportmaster.scd.ui.view.dictionary.DictionaryType.WARE_GROUP;

import java.util.List;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.repository.dictionary.WareGroupRepository;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryItem;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryRegister;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryType;
import ru.sportmaster.scd.ui.view.type.LocalizedProperty;

@Component
@RequiredArgsConstructor
public class WareGroupRegister implements DictionaryRegister {
    private final WareGroupRepository wareGroupRepository;

    @Override
    public DictionaryType getType() {
        return WARE_GROUP;
    }

    @Override
    public Supplier<List<DictionaryItem>> getItems() {
        return () -> wareGroupRepository.findActualWareGroups().stream()
            .map(wareGroup -> DictionaryItem.builder()
                .id(wareGroup.getId())
                .label(LocalizedProperty.of(wareGroup.getName()))
                .value(wareGroup.getId())
                .build()).toList();
    }
}
