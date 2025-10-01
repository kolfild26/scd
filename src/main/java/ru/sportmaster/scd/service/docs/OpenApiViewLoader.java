package ru.sportmaster.scd.service.docs;

import static java.util.Objects.nonNull;
import static ru.sportmaster.scd.consts.OpenApiConst.LOCALIZED_PROP_DESCRIPTION;
import static ru.sportmaster.scd.consts.OpenApiConst.LOCALIZED_PROP_NAME;
import static ru.sportmaster.scd.consts.OpenApiConst.OPEN_API_STRING_TYPE;
import static ru.sportmaster.scd.consts.OpenApiConst.PAGE_OBJECT_NODE_DESCRIPTION;
import static ru.sportmaster.scd.consts.OpenApiConst.SPRING_PAGE_OBJECT_NAME;
import static ru.sportmaster.scd.consts.OpenApiConst.UI_VIEW_TYPE;
import static ru.sportmaster.scd.utils.CollectionUtil.join;
import static ru.sportmaster.scd.utils.JsonUtil.getLocalizedMessage;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.ui.view.UiViewManager;
import ru.sportmaster.scd.ui.view.type.LocalizedProperty;

@Component
@RequiredArgsConstructor
public class OpenApiViewLoader {


    private final UiViewManager viewManager;
    private final MessageSource messageSource;
    private final ModelConverters openApiModelConverter = ModelConverters.getInstance();

    public void load(OpenAPI openAPI) {
        openAPI.getComponents().getSchemas().put(UI_VIEW_TYPE, buildViewEnum());
        openAPI.getComponents().getSchemas().putAll(buildViewObjects());
        documentSpringPageObject(openAPI.getComponents().getSchemas());
        clearLocalizationTypes(openAPI.getComponents().getSchemas());
    }

    private StringSchema buildViewEnum() {
        var views = viewManager.getAllViewNames();

        var viewType = new StringSchema();
        viewType.setDescription("Название view object");
        viewType.setEnum(views);
        if (!views.isEmpty()) {
            viewType.setExample(views.get(0));
        }

        return viewType;
    }

    @SuppressWarnings("rawtypes")
    private Map<String, Schema> buildViewObjects() {
        Map<String, Schema> result = new HashMap<>();

        viewManager.getAllViewNames().forEach(name -> {
            var view = viewManager.getView(name);
            var schemas = openApiModelConverter.readAll(view.getJavaType());
            schemas.forEach(this::setPropertyDescription);
            result.putAll(schemas);
        });

        return result;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void setPropertyDescription(String entityName, Schema schema) {
        var properties = ((Schema<Object>) schema).getProperties();
        properties.forEach((String key, Schema value) -> {
            var localized = LocalizedProperty.of(join(entityName, key), key);
            value.setDescription(getLocalizedMessage(messageSource, localized));
        });
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void clearLocalizationTypes(Map<String, Schema> schemas) {
        schemas.values().forEach(schema -> {
            var properties = ((Schema<Object>) schema).getProperties();
            if (nonNull(properties) && !properties.isEmpty()) {
                properties.forEach((String key, Schema value) -> {
                    var ref = value.get$ref();
                    if (nonNull(ref) && ref.endsWith(LOCALIZED_PROP_NAME)) {
                        value.setType(OPEN_API_STRING_TYPE);
                        value.setDescription(LOCALIZED_PROP_DESCRIPTION);
                        value.set$ref(null);
                    }
                });
            }
        });

        schemas.remove(LOCALIZED_PROP_NAME);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void documentSpringPageObject(Map<String, Schema> schemas) {
        var pageObject = (Schema<Object>) schemas.get(SPRING_PAGE_OBJECT_NAME);
        if (pageObject != null) {
            pageObject.getProperties().forEach((field, schema) -> {
                var description = PAGE_OBJECT_NODE_DESCRIPTION.get(field);
                if (description != null) {
                    schema.setDescription(description);
                }
            });
        }
    }
}
