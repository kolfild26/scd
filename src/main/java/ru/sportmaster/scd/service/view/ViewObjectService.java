package ru.sportmaster.scd.service.view;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryItem;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryType;
import ru.sportmaster.scd.ui.view.type.UIViewFetchFieldRequest;
import ru.sportmaster.scd.ui.view.type.UIViewFetchFieldValuesRequest;
import ru.sportmaster.scd.ui.view.type.UiView;
import ru.sportmaster.scd.ui.view.type.UiViewCrudRequest;
import ru.sportmaster.scd.ui.view.type.UiViewFetchRequest;
import ru.sportmaster.scd.web.UserEnvironment;

public interface ViewObjectService {
    Map<String, UiView> getViews(List<String> viewNames);

    Map<DictionaryType, List<DictionaryItem>> getDictionaries(List<DictionaryType> types);

    Page<ObjectNode> findAll(UiViewFetchRequest request, String sessionUuid, String formUuid);

    List<?> findFieldValues(UIViewFetchFieldValuesRequest request, String sessionUuid, String formUuid);

    Page<ObjectNode> findAllOnlyFields(UIViewFetchFieldRequest request);

    List<ObjectNode> change(List<UiViewCrudRequest> requests, UserEnvironment environment);

    void clear(String view, String formUuid);
}
