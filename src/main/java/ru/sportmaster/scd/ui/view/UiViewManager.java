package ru.sportmaster.scd.ui.view;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import ru.sportmaster.scd.mementity.IMemEntityFilterSelBuilder;
import ru.sportmaster.scd.ui.view.annotation.View;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryItem;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryRegister;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryType;
import ru.sportmaster.scd.ui.view.type.UiView;

@Service
@RequiredArgsConstructor
public class UiViewManager {
    private final ApplicationContext applicationContext;
    private final Map<String, UiView> viewsMap = new ConcurrentHashMap<>();
    private final Map<DictionaryType, DictionaryRegister> dictionaryMap = new ConcurrentHashMap<>();
    private final Map<String, Class<? extends UiViewResolver>> resolverMap = new ConcurrentHashMap<>();
    private final Map<String, Class<? extends UiViewEditor>> editorMap = new ConcurrentHashMap<>();
    private final Map<String, IMemEntityFilterSelBuilder> memFilterSelections = new ConcurrentHashMap<>();

    public List<String> getAllViewNames() {
        return viewsMap.keySet().stream().sorted().toList();
    }

    public UiView getView(String viewName) {
        return viewsMap.get(viewName);
    }

    public UiView getView(Class<?> clazz) {
        return getView(
            getAnnotationViewName(clazz)
        );
    }

    public Supplier<List<DictionaryItem>> getDictionary(DictionaryType dictionaryType) {
        return dictionaryMap.get(dictionaryType).getItems();
    }

    public void putView(UiView view) {
        viewsMap.put(view.getType(), view);
    }

    public boolean isView(String viewName) {
        return viewsMap.containsKey(viewName);
    }

    public boolean isView(Class<?> clazz) {
        return viewsMap.containsKey(
            getAnnotationViewName(clazz)
        );
    }

    public void putDictionary(DictionaryRegister register) {
        dictionaryMap.put(register.getType(), register);
    }

    public UiViewResolver getResolver(String viewName) {
        var resolverClass = Optional.ofNullable(resolverMap.get(viewName)).orElseThrow(() ->
            new IllegalArgumentException(String.format("Not found resolver for View: %s", viewName))
        );
        return applicationContext.getBean(resolverClass);
    }

    public UiViewEditor getEditor(String viewName) {
        var editorClass = Optional.ofNullable(editorMap.get(viewName)).orElseThrow(() ->
            new IllegalArgumentException(String.format("Not found editor for View: %s", viewName))
        );
        return applicationContext.getBean(editorClass);
    }

    public IMemEntityFilterSelBuilder getMemSelection(String viewName) {
        return Optional.ofNullable(memFilterSelections.get(viewName)).orElseThrow(() ->
            new IllegalArgumentException(String.format("Not found mem selection for View: %s", viewName))
        );
    }

    public void putResolver(String type, Class<? extends UiViewResolver> resolver) {
        resolverMap.put(type, resolver);
    }

    public void putEditor(String type, Class<? extends UiViewEditor> editor) {
        editorMap.put(type, editor);
    }

    public void putMemSelection(String type, IMemEntityFilterSelBuilder filterSelection) {
        memFilterSelections.put(type, filterSelection);
    }

    private String getAnnotationViewName(Class<?> clazz) {
        String viewName = clazz.getSimpleName();
        if (!isView(viewName) && clazz.isAnnotationPresent(View.class)) {
            viewName = clazz.getAnnotation(View.class).name();
        }
        return viewName;
    }
}
