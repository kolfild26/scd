package ru.sportmaster.scd.dictionary;

import java.util.List;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.repository.dictionary.CategoryHierarchyRepository;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryItem;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryRegister;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryType;
import ru.sportmaster.scd.ui.view.type.LocalizedProperty;

@Component
@RequiredArgsConstructor
public class CategoryRegister implements DictionaryRegister {
    private final CategoryHierarchyRepository categoryHierarchyRepository;

    @Override
    public DictionaryType getType() {
        return DictionaryType.CATEGORY;
    }

    @Override
    public Supplier<List<DictionaryItem>> getItems() {
        return () -> categoryHierarchyRepository.findNotDeletedCategories().stream().map(
            category -> DictionaryItem.builder()
                .id(category.getId())
                .label(new LocalizedProperty(category.getName()))
                .value(category.getId())
                .build()
        ).toList();
    }
}
