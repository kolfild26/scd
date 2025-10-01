package ru.sportmaster.scd.ui.view.dictionary;

import java.util.List;
import java.util.function.Supplier;

public interface DictionaryRegister {
    DictionaryType getType();

    Supplier<List<DictionaryItem>> getItems();
}
