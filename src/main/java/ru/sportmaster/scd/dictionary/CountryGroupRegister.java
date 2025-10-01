package ru.sportmaster.scd.dictionary;

import static ru.sportmaster.scd.ui.view.dictionary.DictionaryType.COUNTRY_GROUP;

import java.util.List;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.repository.dictionary.CountryGroupRepository;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryItem;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryRegister;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryType;
import ru.sportmaster.scd.ui.view.type.LocalizedProperty;

@Component
@RequiredArgsConstructor
public class CountryGroupRegister implements DictionaryRegister {
    private final CountryGroupRepository countryGroupRepository;

    @Override
    public DictionaryType getType() {
        return COUNTRY_GROUP;
    }

    @Override
    public Supplier<List<DictionaryItem>> getItems() {
        return () -> countryGroupRepository.findNotDeletedCountryGroups().stream()
            .map(countryGroup -> DictionaryItem.builder()
                .id(countryGroup.getId())
                .label(LocalizedProperty.of(countryGroup.getName()))
                .value(countryGroup.getId())
                .build()).toList();
    }
}
