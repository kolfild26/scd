package ru.sportmaster.scd.ui.view.mementity;

import static java.util.Objects.nonNull;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.mementity.IMemEntityFilterSelBuilder;
import ru.sportmaster.scd.ui.view.UiViewManager;
import ru.sportmaster.scd.ui.view.loader.ViewLoader;

@Component
@RequiredArgsConstructor
public class MemEntityLoader extends ViewLoader {
    private final UiViewManager viewManager;
    private final List<IMemEntityFilterSelBuilder> filterSelections;

    @Override
    public void load() {
        if (nonNull(filterSelections)) {
            filterSelections.forEach(item -> viewManager.putMemSelection(item.getType(), item));
        }
    }

    @Override
    public void registerResolver() {
        // Ignore
    }
}
