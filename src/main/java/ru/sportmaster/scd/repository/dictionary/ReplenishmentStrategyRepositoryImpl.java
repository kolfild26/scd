package ru.sportmaster.scd.repository.dictionary;

import java.util.List;
import org.springframework.stereotype.Repository;
import ru.sportmaster.scd.entity.replenishment.ReplenishmentStrategy;
import ru.sportmaster.scd.entity.replenishment.ReplenishmentStrategy_;
import ru.sportmaster.scd.repository.AbstractRepositoryImpl;

@Repository
public class ReplenishmentStrategyRepositoryImpl extends AbstractRepositoryImpl<ReplenishmentStrategy, Long>
        implements ReplenishmentStrategyRepository {
    public ReplenishmentStrategyRepositoryImpl() {
        super(ReplenishmentStrategy.class);
    }

    @Override
    public List<ReplenishmentStrategy> findNotDeletedStrategy() {
        var criteriaBuilder = em.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(ReplenishmentStrategy.class);
        var root = query.from(ReplenishmentStrategy.class);

        var restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(
                restrictions,
                criteriaBuilder.isNull(root.get(ReplenishmentStrategy_.IS_DELETED))
        );
        return em.createQuery(query.select(root).where(restrictions)).getResultList();
    }
}
