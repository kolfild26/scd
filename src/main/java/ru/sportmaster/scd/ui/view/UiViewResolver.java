package ru.sportmaster.scd.ui.view;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.data.domain.Page;
import ru.sportmaster.scd.ui.view.type.UIViewFetchFieldRequest;
import ru.sportmaster.scd.ui.view.type.UIViewFetchFieldValuesRequest;
import ru.sportmaster.scd.ui.view.type.UiViewFetchRequest;

public interface UiViewResolver {
    Page<ObjectNode> findAll(UiViewFetchRequest request, String sessionUuid, String formUuid);

    Stream<?> findAllStream(UiViewFetchRequest request, String sessionUuid, String formUuid);

    List<?> findFieldValuesByPath(UIViewFetchFieldValuesRequest request, String sessionUuid, String formUuid);

    Page<ObjectNode> findAllOnlyFields(UIViewFetchFieldRequest request);

    Page<Object> findAllIds(UiViewFetchRequest request);

    Object findOne(UiViewFetchRequest request);

    void clear(String formUuid);
}
