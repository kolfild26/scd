package ru.sportmaster.scd.dictionary;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.dictionary.Collection;
import ru.sportmaster.scd.repository.dictionary.CollectionRepository;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryItem;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryRegister;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryType;
import ru.sportmaster.scd.ui.view.type.LocalizedProperty;

@Component
@RequiredArgsConstructor
public class CollectionRegister implements DictionaryRegister {
    private final CollectionRepository collectionRepository;

    @Override
    public DictionaryType getType() {
        return DictionaryType.COLLECTION;
    }

    @Override
    public Supplier<List<DictionaryItem>> getItems() {
        LocalDate startPeriod = LocalDate.now().with(firstDayOfCollection()).minusMonths(18);
        return () -> collectionRepository.findActualAfterYearCollections(startPeriod).stream()
            .sorted(Comparator.comparing(Collection::getDateOpen, Comparator.nullsLast(Comparator.naturalOrder())))
            .map(collection -> DictionaryItem.builder()
                .id(collection.getId())
                .value(collection.getId())
                .label(new LocalizedProperty(collection.getName()))
                .build())
            .toList();
    }

    private static TemporalAdjuster firstDayOfCollection() {
        return TemporalAdjusters.ofDateAdjuster(date -> {
            if (date.getMonthValue() < 9) {
                return date.withMonth(3).withDayOfMonth(1);
            } else {
                return date.withDayOfMonth(9).withDayOfMonth(1);
            }
        });
    }
}
