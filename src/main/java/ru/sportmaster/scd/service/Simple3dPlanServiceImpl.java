package ru.sportmaster.scd.service;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.sportmaster.scd.service.simple3dplan.Simple3dPlanDAOService;

@Service
@RequiredArgsConstructor
public class Simple3dPlanServiceImpl implements Simple3dPlanService {
    private final Simple3dPlanDAOService simple3dPlanDAOService;

    @Override
    public void copyPriceLevel(@NonNull Long partitionId,
                               @NonNull Long collectionId,
                               @NonNull Long countryGroupId) {
        simple3dPlanDAOService.copyPriceLevel(partitionId, collectionId, countryGroupId);
    }
}
