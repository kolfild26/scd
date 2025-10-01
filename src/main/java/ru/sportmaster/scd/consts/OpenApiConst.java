package ru.sportmaster.scd.consts;

import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import ru.sportmaster.scd.ui.view.type.LocalizedProperty;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OpenApiConst {
    public static final String UI_VIEW_TYPE = "View";
    public static final String OPEN_API_STRING_TYPE = String.class.getSimpleName().toLowerCase();
    public static final String LOCALIZED_PROP_NAME = LocalizedProperty.class.getSimpleName();
    public static final String SPRING_PAGE_OBJECT_NAME = "PageObjectNode";
    public static final String LOCALIZED_PROP_DESCRIPTION = "Название";
    public static final String UI_VIEW_REF = "#/components/schemas/" + UI_VIEW_TYPE;
    public static final String SESSION_UUID_HEADER_DESCRIPTION = "Идентификатор сессии (открытой формы выбора), "
        + "для view c постфиксом ME. Для view без ME, не используется";
    public static final String FORM_UUID_HEADER_DESCRIPTION = "Идентификатор формы (открытой вкладки), "
        + "для view c постфиксом ME. Для view без ME, не используется";
    public static final Map<String, String> PAGE_OBJECT_NODE_DESCRIPTION = Map.of(
        "totalPages", "Всего страниц",
        "totalElements", "Всего строк",
        "size", "Размер страницы",
        "content", "Данные",
        "first", "Признак первой страницы",
        "last", "Признак последней страницы",
        "numberOfElements", "Количество строк в запросе",
        "pageable", "Пагинация",
        "empty", "Признак пустой страницы"
    );
}
