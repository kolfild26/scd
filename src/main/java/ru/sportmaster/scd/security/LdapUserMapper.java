package ru.sportmaster.scd.security;

import static java.util.Objects.isNull;
import static ru.sportmaster.scd.security.LdapParameters.FIRST_NAME;
import static ru.sportmaster.scd.security.LdapParameters.LAST_NAME;
import static ru.sportmaster.scd.security.LdapParameters.LOGIN;

import java.util.Optional;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import org.springframework.lang.NonNull;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.User;

@Component
public class LdapUserMapper implements AttributesMapper<User> {
    @Override
    public User mapFromAttributes(Attributes attributes) throws NamingException {
        User user = new User();
        user.setLogin(getAttributeValue(getAttribute(attributes, LOGIN)));
        user.setFirstName(getAttributeValue(getAttribute(attributes, FIRST_NAME)));
        user.setLastName(getAttributeValue(getAttribute(attributes, LAST_NAME)));
        return user;
    }

    private String getAttributeValue(Attribute attribute) throws NamingException {
        if (isNull(attribute)) {
            return "";
        }
        return
            Optional.ofNullable(attribute.get())
                .map(String::valueOf)
                .orElse("");
    }

    private Attribute getAttribute(@NonNull Attributes attributes,
                                   @NonNull LdapParameters ldapParameters) {
        return attributes.get(ldapParameters.getValue());
    }
}
