package ru.sportmaster.scd.dictionary;

import static ru.sportmaster.scd.ui.view.dictionary.DictionaryType.ADJUST_DOCTYPE;

import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.adjustment.AdjustmentDocType;
import ru.sportmaster.scd.entity.adjustment.AdjustmentType;
import ru.sportmaster.scd.repository.adjustment.AdjustmentDocTypeRepository;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryItem;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryRegister;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryType;
import ru.sportmaster.scd.ui.view.type.LocalizedProperty;

@Component
@RequiredArgsConstructor
public class AdjustmentDocTypeRegister implements DictionaryRegister {
    private final AdjustmentDocTypeRepository adjustmentDocTypeRepository;

    @Override
    public DictionaryType getType() {
        return ADJUST_DOCTYPE;
    }

    @Override
    public Supplier<List<DictionaryItem>> getItems() {
        return () -> adjustmentDocTypeRepository.findAll().stream()
            .filter(this::existType)
            .sorted(Comparator.comparingLong(this::getOrder))
            .map(type -> DictionaryItem.builder()
                .id(type.getId())
                .label(LocalizedProperty.of(type.getDescription()))
                .value(type.getId())
                .build())
            .toList();
    }

    private long getOrder(AdjustmentDocType type) {
        return AdjustmentType.findById(type.getId()).getOrder();
    }

    private boolean existType(AdjustmentDocType type) {
        return AdjustmentType.hasById(type.getId());
    }
}
