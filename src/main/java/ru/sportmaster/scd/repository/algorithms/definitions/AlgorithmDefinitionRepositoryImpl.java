package ru.sportmaster.scd.repository.algorithms.definitions;

import java.util.List;
import org.springframework.stereotype.Repository;
import ru.sportmaster.scd.entity.algorithms.definitions.AlgorithmDefinition;
import ru.sportmaster.scd.entity.algorithms.definitions.AlgorithmDefinition_;
import ru.sportmaster.scd.repository.AbstractRepositoryImpl;

@Repository
public class AlgorithmDefinitionRepositoryImpl
    extends AbstractRepositoryImpl<AlgorithmDefinition, Long>
    implements AlgorithmDefinitionRepository {
    public AlgorithmDefinitionRepositoryImpl() {
        super(AlgorithmDefinition.class);
    }

    @Override
    public List<AlgorithmDefinition> findAllNotDeleted() {
        var criteriaBuilder = em.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(AlgorithmDefinition.class);
        var root = query.from(AlgorithmDefinition.class);

        var restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(
            restrictions,
            criteriaBuilder.isNull(root.get(AlgorithmDefinition_.IS_DELETED))
        );
        return em.createQuery(query.select(root).where(restrictions)).getResultList();
    }
}
