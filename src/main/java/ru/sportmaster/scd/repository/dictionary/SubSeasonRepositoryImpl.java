package ru.sportmaster.scd.repository.dictionary;

import org.springframework.stereotype.Repository;
import ru.sportmaster.scd.entity.replenishment.SubSeason;
import ru.sportmaster.scd.entity.replenishment.SubSeason_;
import ru.sportmaster.scd.repository.AbstractRepositoryImpl;

@Repository
public class SubSeasonRepositoryImpl extends AbstractRepositoryImpl<SubSeason, Long> implements SubSeasonRepository {
    public SubSeasonRepositoryImpl() {
        super(SubSeason.class);
    }

    @Override
    public SubSeason getByName(String name) {
        var criteriaBuilder = em.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(SubSeason.class);
        var root = query.from(SubSeason.class);

        var restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(
            restrictions,
            criteriaBuilder.equal(root.get(SubSeason_.NAME), name)
        );
        return em.createQuery(query.select(root).where(restrictions))
            .setMaxResults(1)
            .getResultStream()
            .findFirst().orElse(null);
    }
}
