package ru.sportmaster.scd.ui.resolver;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static ru.sportmaster.scd.ui.view.type.Join.OR;
import static ru.sportmaster.scd.ui.view.type.JpaViewSearchKey.buildKey;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.map.IMap;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.dto.view.UiViewSearchRequestDto;
import ru.sportmaster.scd.dto.view.UiViewSearchResponseDto;
import ru.sportmaster.scd.task.HazelCastComponent;
import ru.sportmaster.scd.ui.view.IUiViewSearchProcessor;
import ru.sportmaster.scd.ui.view.UiViewManager;
import ru.sportmaster.scd.ui.view.UiViewResolver;
import ru.sportmaster.scd.ui.view.type.ICondition;
import ru.sportmaster.scd.ui.view.type.JpaViewSearchKey;
import ru.sportmaster.scd.ui.view.type.UIViewCondition;
import ru.sportmaster.scd.ui.view.type.UIViewConditionOperation;
import ru.sportmaster.scd.ui.view.type.UIViewGroupCondition;
import ru.sportmaster.scd.ui.view.type.UiView;
import ru.sportmaster.scd.ui.view.type.UiViewFetchRequest;

@Component
@RequiredArgsConstructor
public class JpaViewSearchProcessor implements IUiViewSearchProcessor {
    private final HazelCastComponent hazelCastComponent;
    private final UiViewManager viewManager;
    private final ObjectMapper objectMapper;

    private IMap<JpaViewSearchKey, List<Integer>> searchPositionsMap;

    @PostConstruct
    public void init() {
        searchPositionsMap = hazelCastComponent.getViewSearchPositionsMap();
    }

    @Override
    public UiViewSearchResponseDto pageSearch(UiViewSearchRequestDto request, Long userId) {
        if (request.isNewSearch()) {
            clearSearch(request.getView(), userId);
        }

        var positions = searchIdsInPage(request);
        if (!positions.isEmpty()) {
            var key = buildKey(request.getView(), userId);
            var list = searchPositionsMap.getOrDefault(key, new ArrayList<>());
            list.addAll(positions);
            searchPositionsMap.put(key, list);
        }

        return UiViewSearchResponseDto.builder()
            .firstIndex(positions.isEmpty() ? null : positions.get(0))
            .totalInPage(positions.size())
            .matches(positions)
            .build();
    }

    @Override
    public int getSearchItemPosition(String view, Long userId, int currentPosition, Byte direction) {
        var positionList = searchPositionsMap.get(buildKey(view, userId));
        if (isNull(positionList) || positionList.isEmpty()) {
            return -1;
        } else {
            return positionList.get(currentPosition + direction);
        }
    }

    @Override
    public void clearSearch(String view, Long userId) {
        searchPositionsMap.remove(buildKey(view, userId));
    }

    public List<Integer> searchIdsInPage(UiViewSearchRequestDto request) {
        List<Integer> positions = new ArrayList<>();

        UiView view = viewManager.getView(request.getView());
        UiViewResolver resolver = viewManager.getResolver(request.getView());

        var searchConditions = buildSearchConditions(view, request.getSearchText());
        var fetchRequest = convertRequest(request);
        fetchRequest.getConditions().add(searchConditions);

        var targetIds = resolver.findAllIds(fetchRequest).getContent();
        if (!targetIds.isEmpty()) {
            fetchRequest.getConditions().remove(searchConditions);
            var totalIds = resolver.findAllIds(fetchRequest).getContent();
            IntStream.range(0, totalIds.size()).forEach(index -> {
                if (targetIds.contains(totalIds.get(index))) {
                    positions.add(index + request.getOffset());
                }
            });
        }

        return positions;
    }

    private UiViewFetchRequest convertRequest(UiViewSearchRequestDto request) {
        List<ICondition> conditions = new ArrayList<>();
        if (nonNull(request.getConditions())) {
            conditions.addAll(request.getConditions());
        }

        return UiViewFetchRequest.builder()
            .view(request.getView())
            .page(request.getPage())
            .size(request.getSize())
            .conditions(conditions)
            .memSelections(request.getMemSelections())
            .sort(request.getSort())
            .build();
    }

    private UIViewGroupCondition buildSearchConditions(UiView view, String searchText) {
        List<ICondition> conditions = new ArrayList<>();

        view.getAttributes().forEach(attribute -> conditions.add(
            UIViewCondition.builder()
                .field(attribute.getName())
                .operation(UIViewConditionOperation.CONTAINS)
                .value(objectMapper.convertValue(searchText, JsonNode.class))
                .build()
        ));

        return UIViewGroupCondition.builder()
            .join(OR)
            .isSearch(true)
            .conditions(conditions)
            .build();
    }
}
