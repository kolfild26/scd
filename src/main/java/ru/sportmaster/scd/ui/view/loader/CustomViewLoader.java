package ru.sportmaster.scd.ui.view.loader;

import static java.util.Optional.ofNullable;
import static ru.sportmaster.scd.utils.ClassUtils.getFieldByName;
import static ru.sportmaster.scd.utils.ClassUtils.getIdFieldName;
import static ru.sportmaster.scd.utils.ClassUtils.hasEmbeddedId;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.ui.view.UIViewBuilder;
import ru.sportmaster.scd.ui.view.UIViewProvider;
import ru.sportmaster.scd.ui.view.UiViewEditor;
import ru.sportmaster.scd.ui.view.UiViewManager;
import ru.sportmaster.scd.ui.view.UiViewResolver;
import ru.sportmaster.scd.ui.view.annotation.CustomView;
import ru.sportmaster.scd.ui.view.type.UiView;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomViewLoader extends ViewLoader {
    private final UiViewManager viewManager;
    private final UIViewBuilder viewBuilder;
    private final Map<String, Class<? extends UiViewResolver>> resolvers = new HashMap<>();
    private final Map<String, Class<? extends UiViewEditor>> editors = new HashMap<>();

    @Override
    public void load() {
        UIViewProvider scanner = new UIViewProvider();
        scanner.addIncludeFilter(new AnnotationTypeFilter(CustomView.class));

        scanner.findCandidateComponents(BASE_PACKAGE).forEach(definition -> {
            Class<?> clazz = getClass(definition);
            CustomView annotation = clazz.getAnnotation(CustomView.class);
            String viewName = annotation.name().isBlank() ? clazz.getSimpleName() : annotation.name();
            UiView view = viewBuilder.build(viewName, clazz, annotation.fetchType());

            String idFieldName = getIdFieldName(clazz);
            Field idField = getFieldByName(clazz, idFieldName);

            view.setIdField(idFieldName);
            view.setIdFieldType(ofNullable(idField).map(Field::getType).orElse(null));
            view.setEmbeddedId(hasEmbeddedId(clazz));
            view.setRecoverable(annotation.recoverable());

            viewManager.putView(view);
            processedViews.add(view.getType());
            resolvers.put(view.getType(), annotation.resolver());
            if (annotation.editor().length == 1) {
                editors.put(view.getType(), annotation.editor()[0]);
            }
        });
    }

    @Override
    public void registerResolver() {
        processedViews.forEach(view -> {
            viewManager.putResolver(view, resolvers.get(view));
            if (editors.containsKey(view)) {
                viewManager.putEditor(view, editors.get(view));
            }
        });
    }
}
