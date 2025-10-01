package ru.sportmaster.scd.repository.adjustment;

import static jakarta.persistence.ParameterMode.IN;
import static jakarta.persistence.ParameterMode.OUT;
import static ru.sportmaster.scd.consts.ParamNames.APPROVE_CORRECTION_PROCEDURE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.CANCEL_CORRECTION_PROCEDURE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.CREATE_CORRECTION_PROCEDURE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.DC_PACKAGE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.P_DESCRIPTION;
import static ru.sportmaster.scd.consts.ParamNames.P_ID_BUSINESS_TMA;
import static ru.sportmaster.scd.consts.ParamNames.P_ID_CORRECTION_LIST;
import static ru.sportmaster.scd.consts.ParamNames.P_ID_CORRECTION_TYPE;
import static ru.sportmaster.scd.consts.ParamNames.P_ID_REF_USER;
import static ru.sportmaster.scd.consts.ParamNames.REMOVE_CORRECTION_PROCEDURE_NAME;
import static ru.sportmaster.scd.consts.ParamNames.SCD_SCHEMA_NAME;
import static ru.sportmaster.scd.consts.ParamNames.UPDATE_CORRECTION_PROCEDURE_NAME;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Repository;
import ru.sportmaster.scd.entity.adjustment.AdjustmentDoc;
import ru.sportmaster.scd.entity.adjustment.AdjustmentDoc_;

@Repository
public class AdjustmentDocRepositoryImpl implements AdjustmentDocRepository {
    private static final String PACKAGE_NAME = SCD_SCHEMA_NAME + "." + DC_PACKAGE_NAME;
    @PersistenceContext
    private EntityManager em;

    @Override
    public AdjustmentDoc findById(Long documentId) {
        var criteriaBuilder = em.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(AdjustmentDoc.class);
        var root = query.from(AdjustmentDoc.class);

        var restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(
            restrictions,
            criteriaBuilder.equal(root.get(AdjustmentDoc_.ID), documentId)
        );
        return em.createQuery(query.select(root).where(restrictions)).getSingleResult();
    }

    @Override
    public Long create(Long typeId, String description, Long businessId, Long userId) {
        StoredProcedureQuery query = em.createStoredProcedureQuery(
            PACKAGE_NAME + "." + CREATE_CORRECTION_PROCEDURE_NAME)
            .registerStoredProcedureParameter(P_ID_CORRECTION_LIST, Long.class, OUT)
            .registerStoredProcedureParameter(P_ID_CORRECTION_TYPE, Long.class, IN)
            .registerStoredProcedureParameter(P_DESCRIPTION, String.class, IN)
            .registerStoredProcedureParameter(P_ID_BUSINESS_TMA, Long.class, IN)
            .registerStoredProcedureParameter(P_ID_REF_USER, Long.class, IN);
        query.setParameter(P_ID_CORRECTION_TYPE, typeId);
        query.setParameter(P_DESCRIPTION, description);
        query.setParameter(P_ID_BUSINESS_TMA, businessId);
        query.setParameter(P_ID_REF_USER, userId);
        query.execute();
        return (Long) query.getOutputParameterValue(P_ID_CORRECTION_LIST);
    }

    @Override
    public void update(Long correctionId, String description, Long userId) {
        StoredProcedureQuery query = em.createStoredProcedureQuery(
            PACKAGE_NAME + "." + UPDATE_CORRECTION_PROCEDURE_NAME)
            .registerStoredProcedureParameter(P_ID_CORRECTION_LIST, Long.class, IN)
            .registerStoredProcedureParameter(P_DESCRIPTION, String.class, IN)
            .registerStoredProcedureParameter(P_ID_REF_USER, Long.class, IN);
        query.setParameter(P_ID_CORRECTION_LIST, correctionId);
        query.setParameter(P_DESCRIPTION, description);
        query.setParameter(P_ID_REF_USER, userId);
        query.execute();
    }

    @Override
    public void delete(Long correctionId, Long userId) {
        StoredProcedureQuery query = em.createStoredProcedureQuery(
            PACKAGE_NAME + "." + REMOVE_CORRECTION_PROCEDURE_NAME)
            .registerStoredProcedureParameter(P_ID_CORRECTION_LIST, Long.class, IN)
            .registerStoredProcedureParameter(P_ID_REF_USER, Long.class, IN);
        query.setParameter(P_ID_CORRECTION_LIST, correctionId);
        query.setParameter(P_ID_REF_USER, userId);
        query.execute();
    }

    @Override
    public void approve(Long correctionId, Long userId) {
        StoredProcedureQuery query = em.createStoredProcedureQuery(
            PACKAGE_NAME + "." + APPROVE_CORRECTION_PROCEDURE_NAME)
            .registerStoredProcedureParameter(P_ID_CORRECTION_LIST, Long.class, IN)
            .registerStoredProcedureParameter(P_ID_REF_USER, Long.class, IN);
        query.setParameter(P_ID_CORRECTION_LIST, correctionId);
        query.setParameter(P_ID_REF_USER, userId);
        query.execute();
    }

    @Override
    public void cancel(Long correctionId, Long userId) {
        StoredProcedureQuery query = em.createStoredProcedureQuery(
                PACKAGE_NAME + "." + CANCEL_CORRECTION_PROCEDURE_NAME)
            .registerStoredProcedureParameter(P_ID_CORRECTION_LIST, Long.class, IN)
            .registerStoredProcedureParameter(P_ID_REF_USER, Long.class, IN);
        query.setParameter(P_ID_CORRECTION_LIST, correctionId);
        query.setParameter(P_ID_REF_USER, userId);
        query.execute();
    }
}
