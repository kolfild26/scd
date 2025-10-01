package ru.sportmaster.scd.service;

import ru.sportmaster.scd.dto.LoginRequestDto;
import ru.sportmaster.scd.dto.UserDto;

public interface AuthService {
    String auth(LoginRequestDto request);

    UserDto getCurrentUser();
}
