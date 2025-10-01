package ru.sportmaster.scd.service.simple3dplan;

import static ru.sportmaster.scd.consts.ParamNames.BACKEND_API_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.PRICE_LEVEL_COPY_PROCEDURE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.P_COLLECTION;
import static ru.sportmaster.scd.consts.ParamNames.P_COUNTRY_GROUP;
import static ru.sportmaster.scd.consts.ParamNames.P_ID_PARTITION_DIV_TMA;
import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class Simple3dPlanDAOServiceImpl implements Simple3dPlanDAOService {
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void copyPriceLevel(@NonNull Long partitionId,
                               @NonNull Long collectionId,
                               @NonNull Long countryGroupId) {
        em
            .createStoredProcedureQuery(
                SCD_SCHEMA_NAME + '.' + BACKEND_API_PACKAGE_NAME + '.'
                    + PRICE_LEVEL_COPY_PROCEDURE_NAME)
            .registerStoredProcedureParameter(
                P_ID_PARTITION_DIV_TMA,
                Long.class,
                ParameterMode.IN)
            .registerStoredProcedureParameter(
                P_COLLECTION,
                Long.class,
                ParameterMode.IN)
            .registerStoredProcedureParameter(
                P_COUNTRY_GROUP,
                Long.class,
                ParameterMode.IN)
            .setParameter(P_ID_PARTITION_DIV_TMA, partitionId)
            .setParameter(P_COLLECTION, collectionId)
            .setParameter(P_COUNTRY_GROUP, countryGroupId)
            .execute();
    }
}
