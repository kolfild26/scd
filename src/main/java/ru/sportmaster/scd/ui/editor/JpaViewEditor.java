package ru.sportmaster.scd.ui.editor;

import static java.util.Arrays.stream;
import static ru.sportmaster.scd.consts.ParamNames.ID;
import static ru.sportmaster.scd.consts.ParamNames.IS_DELETED;
import static ru.sportmaster.scd.utils.ClassUtils.getAnnotation;
import static ru.sportmaster.scd.utils.ClassUtils.getFieldByName;
import static ru.sportmaster.scd.utils.ClassUtils.isAnnotated;
import static ru.sportmaster.scd.utils.ClassUtils.newInstance;
import static ru.sportmaster.scd.utils.ClassUtils.setFieldValue;
import static ru.sportmaster.scd.utils.CollectionUtil.concat;
import static ru.sportmaster.scd.utils.JpaUtil.HIBERNATE_TAG;
import static ru.sportmaster.scd.utils.JsonUtil.convertType;
import static ru.sportmaster.scd.utils.JsonUtil.createInstance;
import static ru.sportmaster.scd.utils.JsonUtil.getId;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import ru.sportmaster.scd.ui.view.UiViewEditor;
import ru.sportmaster.scd.ui.view.UiViewManager;
import ru.sportmaster.scd.ui.view.type.UiView;
import ru.sportmaster.scd.ui.view.type.UiViewCrudRequest;
import ru.sportmaster.scd.utils.JpaUtil;
import ru.sportmaster.scd.utils.JsonUtil;

@Slf4j
@Component
@RequiredArgsConstructor
public class JpaViewEditor implements UiViewEditor {
    private static final Predicate<String> IGNORED_FIELDS = name ->
        !(ID.equals(name) || name.startsWith(HIBERNATE_TAG));
    private static final Map<Class<?>, JsonDeserializer<?>> DESERIALIZERS = new HashMap<>();
    private final UiViewManager viewManager;
    private final ObjectMapper objectMapper;
    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    @Transactional
    public ObjectNode change(UiViewCrudRequest request) {
        UiView view = viewManager.getView(request.getView());

        return switch (request.getType()) {
            case CREATE -> createObject(request, view);
            case UPDATE -> updateObject(request, view);
            case DELETE -> deleteObject(request, view);
            default -> throw new NotImplementedException("Operation not implemented!");
        };
    }

    private ObjectNode createObject(UiViewCrudRequest request, UiView view) {
        Object creatable = createInstance(view.getJavaType());
        merge(request.getValue(), view, creatable);
        entityManager.persist(creatable);
        entityManager.flush();

        return objectMapper.convertValue(creatable, ObjectNode.class);
    }

    private ObjectNode updateObject(UiViewCrudRequest request, UiView view) {
        Object id = JsonUtil.getId(view.getJavaType(), request.getOldValue());
        Object updatable = entityManager.find(view.getJavaType(), id);
        merge(request.getValue(), view, updatable);
        entityManager.merge(updatable);
        entityManager.flush();

        return objectMapper.convertValue(updatable, ObjectNode.class);
    }

    @SneakyThrows
    private ObjectNode deleteObject(UiViewCrudRequest request, UiView view) {
        Object id = JsonUtil.getId(view.getJavaType(), request.getValue());
        Object deletable = entityManager.find(view.getJavaType(), id);

        if (view.isDeletable()) {
            setFieldValue(view.getJavaType(), deletable, IS_DELETED, true);
            entityManager.merge(deletable);
            entityManager.flush();
        } else {
            entityManager.remove(deletable);
        }

        return null;
    }

    public Stream<Field> getFields(Class<?> clazz) {
        return stream(concat(clazz.getFields(), clazz.getDeclaredFields()))
            .filter(field -> !field.isAnnotationPresent(JsonIgnore.class));
    }

    private void merge(JsonNode value, UiView view, Object target) {
        getFields(view.getJavaType()).filter(field -> IGNORED_FIELDS.test(field.getName())).forEach(field -> {
            try {
                Object oldValue = JpaUtil.getValue(target, field.getName());
                Object newValue = null;

                if (viewManager.isView(field.getType()) && !isAnnotated(field, JsonDeserialize.class)) {
                    UiView fieldView = viewManager.getView(field.getType());
                    JsonNode fieldNode = value.get(field.getName());

                    Object fieldViewId = getId(fieldView.getJavaType(), fieldNode);
                    if (fieldViewId != null) {
                        newValue = entityManager.find(field.getType(), fieldViewId);
                    } else if (fieldNode != null && !fieldNode.isNull()) {
                        Object creatable = createInstance(field.getType());
                        merge(fieldNode, fieldView, creatable);
                        newValue = creatable;
                    }
                } else {
                    newValue = getDeserializedValue(value, field);
                }

                if (newValue == null || !Objects.equals(oldValue, newValue)) {
                    setValue(view.getJavaType(), target, field, newValue);
                }
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        });

        if (view.isEmbeddedId()) {
            Field field = getFieldByName(view.getJavaType(), view.getIdField());
            JsonNode fieldNode = value.get(view.getIdField());
            Object newValue = objectMapper.convertValue(fieldNode, view.getIdFieldType());
            setValue(view.getJavaType(), target, field, newValue);
        }
    }

    private Object getDeserializedValue(JsonNode value, Field field) {
        Object newValue;
        JsonNode node = value.get(field.getName());
        Optional<JsonDeserialize> deserialize = getAnnotation(field, JsonDeserialize.class);
        if (deserialize.isPresent()) {
            newValue = deserializeValue(deserialize.get(), field, node);
        } else {
            newValue = convertType(field, value.get(field.getName()));
        }
        return newValue;
    }

    @SneakyThrows
    private Object deserializeValue(JsonDeserialize deserialize, Field field, JsonNode node) {
        Class<?> deserializerType = deserialize.using();
        JsonDeserializer<?> deserializer = DESERIALIZERS.computeIfAbsent(
            field.getType(), key -> (JsonDeserializer<?>) newInstance(deserializerType)
        );
        try (JsonParser parser = new JsonFactory().createParser(node.toString())) {
            parser.nextToken();
            return deserializer.deserialize(parser, objectMapper.getDeserializationContext());
        }
    }

    @SneakyThrows
    private static void setValue(Class<?> type, Object target, Field field, Object newValue) {
        String setterName = "set" + StringUtils.capitalize(field.getName());
        Method setter = ReflectionUtils.findMethod(type, setterName, field.getType());
        if (setter != null) {
            setter.invoke(target, newValue);
        }
    }
}
