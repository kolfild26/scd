package ru.sportmaster.scd.ui.view.loader;

import static java.util.Optional.ofNullable;
import static ru.sportmaster.scd.utils.ClassUtils.getFieldByName;
import static ru.sportmaster.scd.utils.ClassUtils.getIdFieldName;
import static ru.sportmaster.scd.utils.ClassUtils.hasEmbeddedId;

import java.lang.reflect.Field;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.ui.editor.JpaViewEditor;
import ru.sportmaster.scd.ui.resolver.JpaViewResolver;
import ru.sportmaster.scd.ui.view.UIViewBuilder;
import ru.sportmaster.scd.ui.view.UIViewProvider;
import ru.sportmaster.scd.ui.view.UiViewManager;
import ru.sportmaster.scd.ui.view.annotation.View;
import ru.sportmaster.scd.ui.view.type.UiView;

@Slf4j
@Component
@RequiredArgsConstructor
public class JpaViewLoader extends ViewLoader {
    private final UiViewManager viewManager;
    private final UIViewBuilder viewBuilder;

    @Override
    public void load() {
        UIViewProvider scanner = new UIViewProvider();
        scanner.addIncludeFilter(new AnnotationTypeFilter(View.class));

        scanner.findCandidateComponents(BASE_PACKAGE).forEach(definition -> {
            Class<?> clazz = getClass(definition);
            View annotation = clazz.getAnnotation(View.class);
            String viewName = annotation.name().isBlank() ? clazz.getSimpleName() : annotation.name();
            UiView view = viewBuilder.build(viewName, clazz, annotation.fetchType());

            String idFieldName = getIdFieldName(clazz);
            Field idField = getFieldByName(clazz, idFieldName);

            view.setRecoverable(annotation.recoverable());
            view.setIdField(idFieldName);
            view.setIdFieldType(ofNullable(idField).map(Field::getType).orElse(null));
            view.setEmbeddedId(hasEmbeddedId(clazz));

            viewManager.putView(view);
            processedViews.add(view.getType());
        });
    }

    @Override
    public void registerResolver() {
        processedViews.forEach(view -> {
            viewManager.putResolver(view, JpaViewResolver.class);
            viewManager.putEditor(view, JpaViewEditor.class);
        });
    }
}
