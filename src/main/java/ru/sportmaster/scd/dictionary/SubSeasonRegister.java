package ru.sportmaster.scd.dictionary;

import java.util.List;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.repository.dictionary.SubSeasonRepository;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryItem;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryRegister;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryType;
import ru.sportmaster.scd.ui.view.type.LocalizedProperty;

@Component
@RequiredArgsConstructor
public class SubSeasonRegister implements DictionaryRegister {
    private final SubSeasonRepository subSeasonRepository;

    @Override
    public DictionaryType getType() {
        return DictionaryType.SUBSEASON;
    }

    @Override
    public Supplier<List<DictionaryItem>> getItems() {
        return () -> subSeasonRepository.findAll().stream()
                .map(subSeason -> DictionaryItem.builder()
                        .id(subSeason.getId())
                        .value(subSeason.getId())
                        .label(new LocalizedProperty(subSeason.getName()))
                        .build())
                .toList();
    }
}
