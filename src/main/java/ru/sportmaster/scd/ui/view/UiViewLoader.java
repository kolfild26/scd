package ru.sportmaster.scd.ui.view;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sportmaster.scd.ui.view.loader.ViewLoader;

@Service
@RequiredArgsConstructor
public class UiViewLoader {
    private final List<ViewLoader> loaders;

    public void init() {
        loaders.forEach(ViewLoader::load);
        loaders.forEach(ViewLoader::registerResolver);
    }
}
