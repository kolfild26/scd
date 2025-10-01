package ru.sportmaster.scd.ui.view.dictionary;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.ui.view.UiViewManager;
import ru.sportmaster.scd.ui.view.loader.ViewLoader;

@Slf4j
@Component
@RequiredArgsConstructor
public class DictionaryLoader extends ViewLoader {
    private final UiViewManager viewManager;
    private final List<DictionaryRegister> registers;

    @Override
    public void load() {
        registers.forEach(viewManager::putDictionary);
        log.info("{} dictionary loaded!", registers.size());
    }

    @Override
    public void registerResolver() {
        // Ignore
    }
}
