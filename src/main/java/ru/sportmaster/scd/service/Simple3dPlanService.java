package ru.sportmaster.scd.service;

import org.springframework.lang.NonNull;

public interface Simple3dPlanService {
    void copyPriceLevel(@NonNull Long partitionId,
                        @NonNull Long collectionId,
                        @NonNull Long countryGroupId);
}
