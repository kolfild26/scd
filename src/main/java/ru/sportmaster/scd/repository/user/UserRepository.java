package ru.sportmaster.scd.repository.user;

import ru.sportmaster.scd.entity.User;
import ru.sportmaster.scd.repository.AbstractRepository;

public interface UserRepository extends AbstractRepository<User, Long> {
    User findUserByLogin(String login);
}
