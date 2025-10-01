package ru.sportmaster.scd.service.view.io;

import static ru.sportmaster.scd.consts.ParamNames.ID;
import static ru.sportmaster.scd.ui.view.type.ChangeType.CREATE;
import static ru.sportmaster.scd.ui.view.type.ChangeType.UPDATE;
import static ru.sportmaster.scd.ui.view.type.UIViewConditionOperation.EQ;
import static ru.sportmaster.scd.utils.ClassUtils.getAnnotation;
import static ru.sportmaster.scd.utils.ClassUtils.getFieldsStream;
import static ru.sportmaster.scd.utils.ClassUtils.isAnnotated;
import static ru.sportmaster.scd.utils.ClassUtils.isEntity;
import static ru.sportmaster.scd.utils.ClassUtils.setFieldValue;
import static ru.sportmaster.scd.utils.JpaUtil.HIBERNATE_TAG;
import static ru.sportmaster.scd.utils.JsonUtil.convertType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.MapsId;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.ui.view.UiViewManager;
import ru.sportmaster.scd.ui.view.type.ICondition;
import ru.sportmaster.scd.ui.view.type.UIViewCondition;
import ru.sportmaster.scd.ui.view.type.UiView;
import ru.sportmaster.scd.ui.view.type.UiViewCrudRequest;
import ru.sportmaster.scd.ui.view.type.UiViewFetchRequest;
import ru.sportmaster.scd.utils.JsonUtil;

@Slf4j
@Component
@RequiredArgsConstructor
public class ViewRequestBuilder {
    private final ObjectMapper objectMapper;
    private final UiViewManager viewManager;

    public UiViewCrudRequest buildCreateRequest(UiView view, ObjectNode node) {
        return UiViewCrudRequest.builder()
            .view(view.getType())
            .type(CREATE)
            .value(createNewValues(view, node))
            .build();
    }

    public UiViewCrudRequest buildUpdateRequest(UiView view, ObjectNode node, Object old) {
        return UiViewCrudRequest.builder()
            .view(view.getType())
            .type(UPDATE)
            .value(createNewValues(view, node))
            .oldValue(objectMapper.convertValue(old, ObjectNode.class))
            .build();
    }

    private ObjectNode createNewValues(UiView view, ObjectNode node) {
        var viewType = view.getJavaType();
        var result = JsonUtil.createInstance(viewType);
        putValuesId(view, node, viewType, result);

        getFieldsStream(view.getJavaType()).forEach(field -> {
            var fieldName = field.getName();

            if (!isAnnotated(field, EmbeddedId.class)) {
                if (isEntity(field.getType())) {
                    var entity = findEntity(field, node);
                    setFieldValue(viewType, result, fieldName, entity);
                } else if (!fieldName.startsWith(HIBERNATE_TAG)) {
                    var value = node.get(fieldName);
                    var convertedValue = convertType(field.getType(), value);
                    setFieldValue(viewType, result, fieldName, convertedValue);
                }
            }
        });

        return objectMapper.convertValue(result, ObjectNode.class);
    }

    private void putValuesId(UiView view, ObjectNode node, Class<?> viewType, Object result) {
        var idPath = view.getIdField();
        var idValue = node.get(idPath);
        Object convertedValue;
        if (view.isEmbeddedId()) {
            convertedValue = objectMapper.convertValue(idValue, view.getIdFieldType());
        } else {
            convertedValue = convertType(view.getIdFieldType(), idValue);
        }
        setFieldValue(viewType, result, idPath, convertedValue);
    }

    private Object findEntity(Field type, ObjectNode node) {
        var view = type.getType().getSimpleName();
        var resolver = viewManager.getResolver(view);
        var conditions = buildConditions(type, node);

        return resolver.findOne(UiViewFetchRequest.builder()
            .view(view)
            .size(1)
            .conditions(conditions)
            .build());
    }

    private List<ICondition> buildConditions(Field field, ObjectNode node) {
        List<ICondition> conditions = new ArrayList<>();
        if (isAnnotated(field, MapsId.class)) {
            String val = getAnnotation(field, MapsId.class).map(MapsId::value).orElseThrow();
            conditions.add(UIViewCondition.builder()
                .field(ID)
                .operation(EQ)
                .value(node.get(ID).get(val))
                .build());
        } else {
            node.fieldNames().forEachRemaining(name -> {
                if (name.startsWith(field.getName())) {
                    conditions.add(UIViewCondition.builder()
                        .field(name.replace(field + ".", ""))
                        .operation(EQ)
                        .value(node.get(name))
                        .build());
                }
            });
        }

        return conditions;
    }
}
