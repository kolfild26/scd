package ru.sportmaster.scd.repository.dictionary;

import java.util.List;
import org.springframework.stereotype.Repository;
import ru.sportmaster.scd.entity.adjustment.Business_;
import ru.sportmaster.scd.entity.dictionary.DivisionSCD_;
import ru.sportmaster.scd.entity.dictionary.Partition;
import ru.sportmaster.scd.entity.dictionary.Partition_;
import ru.sportmaster.scd.repository.AbstractRepositoryImpl;

@Repository
public class PartitionRepositoryImpl
    extends AbstractRepositoryImpl<Partition, Long>
    implements PartitionRepository {
    public PartitionRepositoryImpl() {
        super(Partition.class);
    }

    @Override
    public List<Partition> findByBusiness(Long idBusiness) {
        var criteriaBuilder = em.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Partition.class);
        var root = query.from(Partition.class);
        var divisions = root.join(Partition_.division);
        var business = divisions.join(DivisionSCD_.business);
        var restrictions =
            criteriaBuilder.equal(
                business.get(Business_.id),
                idBusiness
            );
        return em.createQuery(query.select(root).where(restrictions)).getResultList();
    }
}
