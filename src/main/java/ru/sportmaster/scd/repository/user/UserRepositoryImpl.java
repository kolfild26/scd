package ru.sportmaster.scd.repository.user;

import org.springframework.stereotype.Repository;
import ru.sportmaster.scd.entity.User;
import ru.sportmaster.scd.entity.User_;
import ru.sportmaster.scd.repository.AbstractRepositoryImpl;

@Repository
public class UserRepositoryImpl extends AbstractRepositoryImpl<User, Long> implements UserRepository {
    public UserRepositoryImpl() {
        super(User.class);
    }

    @Override
    public User findUserByLogin(String login) {
        var criteriaBuilder = em.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(User.class);
        var root = query.from(User.class);

        var restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(
            restrictions,
            criteriaBuilder.equal(
                root.get(User_.LOGIN),
                login
            )
        );
        var resultQuery = em.createQuery(query.select(root).distinct(true).where(restrictions));
        return resultQuery.getResultList().stream().findFirst().orElse(null);
    }
}
