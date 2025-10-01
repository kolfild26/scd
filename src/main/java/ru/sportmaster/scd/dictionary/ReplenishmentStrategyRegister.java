package ru.sportmaster.scd.dictionary;

import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.replenishment.ReplenishmentStrategy;
import ru.sportmaster.scd.repository.dictionary.ReplenishmentStrategyRepository;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryItem;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryRegister;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryType;
import ru.sportmaster.scd.ui.view.type.LocalizedProperty;

@Component
@RequiredArgsConstructor
public class ReplenishmentStrategyRegister implements DictionaryRegister {
    private final ReplenishmentStrategyRepository replenishmentStrategyRepository;

    @Override
    public DictionaryType getType() {
        return DictionaryType.REPL_STRATEGY;
    }

    @Override
    public Supplier<List<DictionaryItem>> getItems() {
        return () -> replenishmentStrategyRepository.findNotDeletedStrategy().stream()
            .sorted(Comparator.comparingLong(ReplenishmentStrategy::getOrder))
            .map(strategy -> DictionaryItem.builder()
                .id(strategy.getId())
                .value(strategy.getId())
                .label(new LocalizedProperty(strategy.getName()))
                .build())
            .toList();
    }
}
