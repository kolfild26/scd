package ru.sportmaster.scd.service.view;

import static java.util.Objects.nonNull;
import static ru.sportmaster.scd.utils.UiUtil.findEntityId;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.dto.pivot.HierarchyDto;
import ru.sportmaster.scd.entity.dictionary.IShopHierarchy;
import ru.sportmaster.scd.repository.dictionary.ShopOSHierarchyRepository;
import ru.sportmaster.scd.repository.dictionary.ShopSMHierarchyRepository;
import ru.sportmaster.scd.ui.view.UiViewResolver;
import ru.sportmaster.scd.ui.view.type.UIViewFetchFieldRequest;
import ru.sportmaster.scd.ui.view.type.UIViewFetchFieldValuesRequest;
import ru.sportmaster.scd.ui.view.type.UiViewFetchRequest;

@Component
@RequiredArgsConstructor
public class HierarchyResolver implements UiViewResolver {
    private static final String BUSINESS = "business";
    private static final long SM_BUSINESS_ID = 120890299;
    private final ObjectMapper objectMapper;
    private final ShopSMHierarchyRepository shopSMHierarchyRepository;
    private final ShopOSHierarchyRepository shopOSHierarchyRepository;

    @Override
    public Page<ObjectNode> findAll(UiViewFetchRequest request, String sessionUuid, String formUuid) {
        Long businessId = findEntityId(request.getConditions(), BUSINESS);
        List<ObjectNode> result = new ArrayList<>();

        List<? extends IShopHierarchy> entities = getShopHierarchyByBusinessId(businessId);
        if (entities != null && !entities.isEmpty()) {
            result.add(objectMapper.convertValue(buildTree(entities), ObjectNode.class));
        }

        return new PageImpl<>(result);
    }

    @Override
    public Stream<?> findAllStream(UiViewFetchRequest request, String sessionUuid, String formUuid) {
        throw new NotImplementedException("findAllStream(request, sessionUuid, formUuid) not implemented");
    }

    @Override
    public Page<ObjectNode> findAllOnlyFields(UIViewFetchFieldRequest request) {
        throw new NotImplementedException("findAllOnlyFields(request) not implemented");
    }

    @Override
    public Page<Object> findAllIds(UiViewFetchRequest request) {
        throw new NotImplementedException("findAllIds(request) not implemented");
    }

    @Override
    public Object findOne(UiViewFetchRequest request) {
        throw new NotImplementedException("findOne(request) not implemented");
    }

    @Override
    public List<?> findFieldValuesByPath(UIViewFetchFieldValuesRequest request, String sessionUuid, String formUuid) {
        throw new NotImplementedException("findFieldValuesByPath(request) not implemented");
    }

    @Override
    public void clear(String formUuid) {
        throw new NotImplementedException("clear(formUuid) not implemented");
    }

    private HierarchyDto buildTree(@NotNull List<? extends IShopHierarchy> entities) {
        Long totalEntityId = entities.get(0).getId();

        Map<Long, HierarchyDto> treeMap = new HashMap<>();
        entities.forEach(entity -> {
            HierarchyDto child = HierarchyDto.builder().id(entity.getId()).name(entity.getMoniker()).build();

            Long parentId = entity.getParentId();
            if (parentId != null && treeMap.containsKey(parentId)) {
                HierarchyDto parent = treeMap.get(entity.getParentId());
                parent.getChildren().add(child);
            }

            treeMap.put(entity.getId(), child);

        });

        return treeMap.get(totalEntityId);
    }

    private List<? extends IShopHierarchy> getShopHierarchyByBusinessId(Long businessId) {
        return nonNull(businessId) && SM_BUSINESS_ID != businessId
            ? shopOSHierarchyRepository.findSortedForTree()
            : shopSMHierarchyRepository.findSortedForTree();
    }
}
