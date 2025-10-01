package ru.sportmaster.scd.dictionary;

import static ru.sportmaster.scd.ui.view.dictionary.DictionaryType.DISTR_SOURCE;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.simple3dplan.DistrSource;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryItem;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryRegister;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryType;
import ru.sportmaster.scd.ui.view.type.LocalizedProperty;

@Component
@RequiredArgsConstructor
public class DistrSourceRegister implements DictionaryRegister {
    @Override
    public DictionaryType getType() {
        return DISTR_SOURCE;
    }

    @Override
    public Supplier<List<DictionaryItem>> getItems() {
        return () ->
            Stream.of(DistrSource.values())
                .map(
                    distrSource -> DictionaryItem.builder()
                        .id((long) distrSource.ordinal())
                        .label(LocalizedProperty.of(distrSource.getValue()))
                        .value(distrSource.name())
                        .build()
                )
                .toList();
    }
}
