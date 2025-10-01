package ru.sportmaster.scd.dictionary;

import static ru.sportmaster.scd.ui.view.dictionary.DictionaryType.BUSINESS;

import java.util.List;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.repository.dictionary.BusinessRepository;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryItem;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryRegister;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryType;
import ru.sportmaster.scd.ui.view.type.LocalizedProperty;

@Component
@RequiredArgsConstructor
public class BusinessRegister implements DictionaryRegister {
    private static final String SYSTEM_BUSINESS_LABEL = "No";
    private final BusinessRepository businessRepository;

    @Override
    public DictionaryType getType() {
        return BUSINESS;
    }

    @Override
    public Supplier<List<DictionaryItem>> getItems() {
        return () -> businessRepository.findNotDeletedBusinesses().stream()
            .filter(business -> !SYSTEM_BUSINESS_LABEL.equals(business.getNameEng()))
            .map(business -> DictionaryItem.builder()
                .id(business.getId())
                .label(LocalizedProperty.of(business.getName()))
                .value(business.getId())
                .build()
        ).toList();
    }
}
