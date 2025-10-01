package ru.sportmaster.scd.service.view;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sportmaster.scd.ui.view.UiViewManager;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryItem;
import ru.sportmaster.scd.ui.view.dictionary.DictionaryType;
import ru.sportmaster.scd.ui.view.type.UIViewFetchFieldRequest;
import ru.sportmaster.scd.ui.view.type.UIViewFetchFieldValuesRequest;
import ru.sportmaster.scd.ui.view.type.UiView;
import ru.sportmaster.scd.ui.view.type.UiViewCrudRequest;
import ru.sportmaster.scd.ui.view.type.UiViewFetchRequest;
import ru.sportmaster.scd.web.UserEnvironment;

@Service
@RequiredArgsConstructor
public class ViewObjectServiceImpl implements ViewObjectService {
    private final UiViewManager viewManager;

    @Override
    public Map<String, UiView> getViews(List<String> viewNames) {
        if (viewNames == null || viewNames.isEmpty()) {
            return Collections.emptyMap();
        }
        return viewNames.stream()
            .map(i -> Map.entry(i, viewManager.getView(i)))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Map<DictionaryType, List<DictionaryItem>> getDictionaries(List<DictionaryType> types) {
        Map<DictionaryType, List<DictionaryItem>> result = new HashMap<>();
        if (types != null) {
            types.forEach(type -> result.put(type, viewManager.getDictionary(type).get()));
        }
        return result;
    }

    @Override
    public List<?> findFieldValues(UIViewFetchFieldValuesRequest request, String sessionUuid, String formUuid) {
        return viewManager.getResolver(request.getView()).findFieldValuesByPath(request, sessionUuid, formUuid);
    }

    @Override
    @Transactional
    public Page<ObjectNode> findAll(UiViewFetchRequest request, String sessionUuid, String formUuid) {
        return viewManager.getResolver(request.getView()).findAll(request, sessionUuid, formUuid);
    }

    @Override
    @Transactional
    public Page<ObjectNode> findAllOnlyFields(UIViewFetchFieldRequest request) {
        return viewManager.getResolver(request.getView()).findAllOnlyFields(request);
    }

    @Override
    @Transactional
    public List<ObjectNode> change(List<UiViewCrudRequest> requests, UserEnvironment environment) {
        List<ObjectNode> result = new ArrayList<>();
        if (requests != null && !requests.isEmpty()) {
            requests.forEach(request -> {
                result.add(viewManager.getEditor(request.getView()).change(request));
            });
        }

        return result;
    }

    @Override
    public void clear(String view, String formUuid) {
        viewManager.getResolver(view).clear(formUuid);
    }
}
