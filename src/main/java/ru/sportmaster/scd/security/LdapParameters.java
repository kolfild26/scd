package ru.sportmaster.scd.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LdapParameters {
    LOGIN("name"),
    FIRST_NAME("givenname"),
    LAST_NAME("sn");

    private String value;
}
