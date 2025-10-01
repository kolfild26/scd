package ru.sportmaster.scd.repository.adjustment;

import org.springframework.stereotype.Repository;
import ru.sportmaster.scd.entity.adjustment.AdjustmentUser;
import ru.sportmaster.scd.entity.adjustment.AdjustmentUser_;
import ru.sportmaster.scd.repository.AbstractRepositoryImpl;

@Repository
public class AdjustmentUserRepositoryImpl extends AbstractRepositoryImpl<AdjustmentUser, Long>
    implements AdjustmentUserRepository {
    public AdjustmentUserRepositoryImpl() {
        super(AdjustmentUser.class);
    }

    @Override
    public Long getDcUserId(Long userId) {
        var criteriaBuilder = em.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(AdjustmentUser.class);
        var root = query.from(AdjustmentUser.class);

        var restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(
            restrictions,
            criteriaBuilder.equal(root.get(AdjustmentUser_.USER_ID), userId)
        );
        return em.createQuery(query.select(root).where(restrictions))
            .setMaxResults(1)
            .getResultStream()
            .findFirst()
            .map(AdjustmentUser::getId)
            .orElse(null);
    }
}
