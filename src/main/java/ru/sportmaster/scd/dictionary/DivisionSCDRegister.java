package ru.sportmaster.scd.dictionary;

import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.dictionary.DivisionSCD;
import ru.sportmaster.scd.repository.dictionary.DivisionSCDRepository;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryItem;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryRegister;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryType;
import ru.sportmaster.scd.ui.view.type.LocalizedProperty;

@Component
@RequiredArgsConstructor
public class DivisionSCDRegister implements DictionaryRegister {
    private final DivisionSCDRepository divisionSCDRepository;

    @Override
    public DictionaryType getType() {
        return DictionaryType.DIVISION;
    }

    @Override
    public Supplier<List<DictionaryItem>> getItems() {
        return () -> divisionSCDRepository.findAll().stream()
            .sorted(Comparator.comparing(DivisionSCD::getId))
            .map(division -> DictionaryItem.builder()
                .id(division.getId())
                .value(division.getId())
                .label(LocalizedProperty.of(division.getDescription()))
                .build())
            .toList();
    }
}
