package ru.sportmaster.scd.service.planlimit;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sportmaster.scd.entity.dictionary.Collection;
import ru.sportmaster.scd.repository.planlimit.PlanLimitSettingsRepository;

@Service
@RequiredArgsConstructor
public class PlanLimitSettingsServiceImpl implements PlanLimitSettingsService {
    private final PlanLimitSettingsRepository planLimitSettingsRepository;

    @Override
    public List<Collection> getAvailableCollections(Long divisionId) {
        return planLimitSettingsRepository.getExistingCollections(divisionId);
    }
}
