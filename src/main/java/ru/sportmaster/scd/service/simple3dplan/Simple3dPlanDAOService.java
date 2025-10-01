package ru.sportmaster.scd.service.simple3dplan;

import org.springframework.lang.NonNull;

public interface Simple3dPlanDAOService {
    void copyPriceLevel(@NonNull Long partitionId,
                        @NonNull Long collectionId,
                        @NonNull Long countryGroupId);
}
