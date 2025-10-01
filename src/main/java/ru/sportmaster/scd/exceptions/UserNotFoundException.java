package ru.sportmaster.scd.exceptions;

import org.springframework.security.authentication.BadCredentialsException;

public class UserNotFoundException extends BadCredentialsException {
    private static final String MESSAGE = "Неверный логин или пароль";

    public UserNotFoundException() {
        super(MESSAGE);
    }

    public UserNotFoundException(Exception ex) {
        super(MESSAGE, ex);
    }
}
