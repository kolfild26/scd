package ru.sportmaster.scd.service.replenishment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sportmaster.scd.entity.replenishment.KgtSettings_;
import ru.sportmaster.scd.exceptions.BadRequestException;
import ru.sportmaster.scd.repository.replenishment.KgtSettingsAttrValueRepository;

@Service
@RequiredArgsConstructor
public class ReplSettingsServiceImpl implements ReplSettingsService {
    private final ObjectMapper objectMapper;
    private final KgtSettingsAttrValueRepository kgtSettingsAttrValueRepository;

    @Override
    public List<ObjectNode> getKgtAttrTreeValues(String level, Long id) {
        List<?> result = switch (level) {
            case KgtSettings_.WARE_GROUP -> kgtSettingsAttrValueRepository.findAllWareGroup();
            case KgtSettings_.CATEGORY -> kgtSettingsAttrValueRepository.findAllCategoryByWareGroup(id);
            case KgtSettings_.SUB_CATEGORY -> kgtSettingsAttrValueRepository.findAllSubcategoryByCategory(id);
            case KgtSettings_.WARE_CLASS -> kgtSettingsAttrValueRepository.findAllWareClassBySubCategory(id);
            default -> throw new BadRequestException("Parameter not found!");
        };

        return result.stream().map(i -> objectMapper.convertValue(i, ObjectNode.class)).toList();
    }
}
