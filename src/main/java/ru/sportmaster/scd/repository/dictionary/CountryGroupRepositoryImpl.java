package ru.sportmaster.scd.repository.dictionary;

import java.util.List;
import org.springframework.stereotype.Repository;
import ru.sportmaster.scd.entity.pivot.CountryGroup;
import ru.sportmaster.scd.entity.pivot.CountryGroup_;
import ru.sportmaster.scd.repository.AbstractRepositoryImpl;

@Repository
public class CountryGroupRepositoryImpl
    extends AbstractRepositoryImpl<CountryGroup, Long>
    implements CountryGroupRepository {
    public CountryGroupRepositoryImpl() {
        super(CountryGroup.class);
    }

    @Override
    public List<CountryGroup> findNotDeletedCountryGroups() {
        var criteriaBuilder = em.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(CountryGroup.class);
        var root = query.from(CountryGroup.class);

        var restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(
            restrictions,
            criteriaBuilder.isNull(root.get(CountryGroup_.IS_DELETED))
        );
        return em.createQuery(query.select(root).where(restrictions)).getResultList();
    }
}
