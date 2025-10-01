package ru.sportmaster.scd.ui.view;

import static java.util.Comparator.comparing;
import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsLast;
import static java.util.Objects.nonNull;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static org.springframework.util.ClassUtils.isPrimitiveOrWrapper;
import static ru.sportmaster.scd.utils.ClassUtils.getFieldsStream;
import static ru.sportmaster.scd.utils.ClassUtils.isAnnotated;
import static ru.sportmaster.scd.utils.ClassUtils.isCollectionEntity;
import static ru.sportmaster.scd.utils.ClassUtils.isEntity;
import static ru.sportmaster.scd.utils.ClassUtils.isEnum;
import static ru.sportmaster.scd.utils.CollectionUtil.join;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.ui.view.annotation.View;
import ru.sportmaster.scd.ui.view.annotation.ViewField;
import ru.sportmaster.scd.ui.view.annotation.ViewRenderer;
import ru.sportmaster.scd.ui.view.type.DataFetchType;
import ru.sportmaster.scd.ui.view.type.LocalizedProperty;
import ru.sportmaster.scd.ui.view.type.UIViewAttributeEditor;
import ru.sportmaster.scd.ui.view.type.UiView;
import ru.sportmaster.scd.ui.view.type.UiViewAttribute;
import ru.sportmaster.scd.utils.CollectionUtil;

@Slf4j
@Component
public class UIViewBuilder {
    private static final Predicate<Field> ALLOWED_FIELDS = field ->
        isEntity(field.getType())
        || isEnum(field.getType())
        || isCollectionEntity(field)
        || field.getType().equals(Date.class)
        || field.getType().equals(LocalDate.class)
        || field.getType().equals(LocalDateTime.class)
        || field.getType().equals(String.class)
        || isPrimitiveOrWrapper(field.getType());

    public UiView build(String name, Class<?> clazz, DataFetchType fetchType) {
        return UiView.builder(name, clazz)
            .fetchType(fetchType)
            .attributes(buildAttributes(clazz, "", null, clazz))
            .build();
    }

    public List<UiViewAttribute> buildAttributes(Class<?> clazz, String parentPath,
                                                 Class<?> parent, Class<?> source) {
        List<UiViewAttribute> attributes = new ArrayList<>();

        List<Field> fields = getFields(clazz);
        for (Field field : fields) {
            if (!Objects.equals(parent, field.getType())) {
                String path = CollectionUtil.join(parentPath, field.getName());
                LocalizedProperty label = new LocalizedProperty(
                    join(source.getSimpleName(), path),
                    path,
                    join(clazz.getSimpleName(), field.getName()),
                    field.getName()
                );
                String type = findFieldType(field);

                ViewField fieldAnnotation = getAnnotation(field, ViewField.class).orElse(null);
                Boolean editable = fieldAnnotation == null || fieldAnnotation.editable() ? null : false;
                boolean required = fieldAnnotation == null || fieldAnnotation.required();
                ViewRenderer viewRenderer = null;
                UIViewAttributeEditor editor = null;

                if (fieldAnnotation != null && fieldAnnotation.renderer().length != 0) {
                    viewRenderer = fieldAnnotation.renderer()[0];
                    if (viewRenderer.updType() != null && viewRenderer.updType().length != 0) {
                        Class<?> updClass = viewRenderer.updType()[0];

                        if (isEntity(updClass)) {
                            editor = buildEditor(viewRenderer.updField(), updClass.getSimpleName(), fieldAnnotation);
                        } else {
                            type = updClass.getSimpleName();
                        }
                    }
                }

                UiViewAttribute attribute = UiViewAttribute.builder()
                    .type(type)
                    .sourceType(clazz)
                    .javaType(field.getType())
                    .name(path)
                    .label(label)
                    .required(required)
                    .editor(editor)
                    .build();

                if (isAnnotated(field, View.class)) {
                    View annotation = getAnnotation(field, View.class).orElseThrow();
                    List<UiViewAttribute> children = buildAttributes(field.getType(), path, clazz, source);

                    if (viewRenderer != null) {
                        String updType = type;
                        String updField = join(path, viewRenderer.updField());

                        if (!viewRenderer.updField().isBlank()) {
                            updType = children.stream()
                                .filter(child -> Objects.equals(updField, child.getName()))
                                .map(UiViewAttribute::getType)
                                .findFirst().orElse(null);
                        }

                        if (fieldAnnotation.editable()) {
                            editor = buildEditor(updField, updType, fieldAnnotation);
                        }

                        if (!viewRenderer.srcField().isBlank()) {
                            String visibleField = join(path, viewRenderer.srcField());
                            children = children.stream()
                                .filter(child -> Objects.equals(visibleField, child.getName()))
                                .toList();
                        }

                        if (viewRenderer.srcType() != null && viewRenderer.srcType().length == 1) {
                            attribute.setType(viewRenderer.srcType()[0].getSimpleName());
                        }
                    }

                    if (annotation.flat()) {
                        for (UiViewAttribute child : children) {
                            child.setRequired(of(attribute.isRequired()).orElse(child.isRequired()));
                            child.setOrder(ofNullable(attribute.getOrder()).orElse(child.getOrder()));
                            child.setEditor(ofNullable(child.getEditor()).orElse(editor));
                            child.setEditable(ofNullable(child.getEditable()).orElse(editable));
                        }
                        attributes.addAll(children);
                    } else {
                        attribute.setEditor(ofNullable(attribute.getEditor()).orElse(editor));
                        attribute.setEditable(ofNullable(attribute.getEditable()).orElse(editable));
                        attribute.setChildren(children);
                        attributes.add(attribute);
                    }
                } else {
                    if (hasRules(fieldAnnotation)) {
                        attribute.setEditor(buildEditor(attribute.getName(), attribute.getType(), fieldAnnotation));
                    }
                    attribute.setEditable(editable);
                    attributes.add(attribute);
                }

                attribute.setOrder(fieldAnnotation != null ? fieldAnnotation.order() : Integer.MAX_VALUE);
            }
        }
        attributes.sort(comparing(UiViewAttribute::getOrder, nullsLast(naturalOrder())));

        return attributes;
    }

    private String findFieldType(Field field) {
        if (field.isAnnotationPresent(View.class)
            && !field.getAnnotation(View.class).name().isBlank()) {
            return field.getAnnotation(View.class).name();
        }
        if (field.getType().isAnnotationPresent(View.class)
            && !field.getType().getAnnotation(View.class).name().isBlank()) {
            return field.getType().getAnnotation(View.class).name();
        }
        return field.getType().getSimpleName();
    }

    public <T extends Annotation> Optional<T> getAnnotation(Field field, Class<T> annotation) {
        if (field.isAnnotationPresent(annotation)) {
            return of(field.getAnnotation(annotation));
        } else if (field.getType().isAnnotationPresent(annotation)) {
            return of(field.getType().getAnnotation(annotation));
        }
        return empty();
    }

    @NotNull
    public static List<Field> getFields(Class<?> clazz) {
        var fields = getFieldsStream(clazz)
            .filter(ALLOWED_FIELDS)
            .filter(field -> !field.isAnnotationPresent(JsonIgnore.class))
            .toList();

        if (fields.stream().anyMatch(field -> field.isAnnotationPresent(ViewField.class))) {
            return fields.stream()
                .filter(field -> field.isAnnotationPresent(ViewField.class) || field.isAnnotationPresent(View.class))
                .toList();
        }
        return fields;
    }

    private UIViewAttributeEditor buildEditor(String field, String type, ViewField fieldAnnotation) {
        var builder = UIViewAttributeEditor.builder().name(field).type(type);

        if (hasRules(fieldAnnotation)) {
            var rule = fieldAnnotation.rules()[0];
            builder.dayOfWeek(of(rule.dayOfWeek()).filter(i -> i > 0).orElse(null));
            builder.calendarType(of(rule.calendarType()).orElse(null));
        }

        return builder.build();
    }

    private boolean hasRules(ViewField fieldAnnotation) {
        return nonNull(fieldAnnotation) && fieldAnnotation.rules().length == 1;
    }
}
