package ru.sportmaster.scd.security;

import static java.lang.String.format;
import static ru.sportmaster.scd.security.LdapParameters.LOGIN;
import static ru.sportmaster.scd.utils.StreamUtils.readBytes;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.sportmaster.scd.entity.User;
import ru.sportmaster.scd.exceptions.UserNotFoundException;
import ru.sportmaster.scd.repository.user.UserRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthProvider implements AuthenticationProvider {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LdapUserMapper ldapUserMapper;
    @Value("${active-directory.urls}")
    private String[] urls;
    @Value("${active-directory.domain}")
    private String domain;
    @Value("${active-directory.base}")
    private String base;
    @Value("${active-directory.store.certificates}")
    private List<String> certificates;
    @Value("${active-directory.store.password}")
    private String certificateStorePassword;

    @SneakyThrows
    public void init() {
        KeyStore store = KeyStore.getInstance("JKS");
        store.load(null, certificateStorePassword.toCharArray());

        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        for (int i = 0; i < certificates.size(); i++) {
            var stream = new ByteArrayInputStream(readBytes(certificates.get(i)));
            Certificate certificate = certificateFactory.generateCertificate(stream);
            store.setCertificateEntry("ldaps-" + i, certificate);
        }

        File keyStoreFile = new File("ldap-key-store.jks");
        if (!keyStoreFile.exists()) {
            keyStoreFile.createNewFile();
            try (FileOutputStream fos = new FileOutputStream(keyStoreFile)) {
                store.store(fos, certificateStorePassword.toCharArray());
            }
        }

        System.setProperty("javax.net.ssl.trustStore", keyStoreFile.getAbsolutePath());
        System.setProperty("javax.net.ssl.trustStorePassword", certificateStorePassword);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String login = authentication.getName();
        String password = authentication.getCredentials().toString();

        if (login != null) {
            User user = userRepository.findUserByLogin(login);

            if (user != null) {
                try {
                    if (user.isTest()) {
                        if (passwordEncoder.matches(password, user.getPassword())) {
                            return new UsernamePasswordAuthenticationToken(user, null, List.of());
                        } else {
                            throw new UserNotFoundException();
                        }
                    } else {
                        LdapTemplate template = buildTemplate(login, password);
                        LdapQuery query = LdapQueryBuilder.query().countLimit(1).where(LOGIN.getValue()).is(login);
                        var result = template.search(query, ldapUserMapper);

                        if (result != null && result.size() == 1) {
                            return new UsernamePasswordAuthenticationToken(user, null, List.of());
                        }
                    }
                } catch (Exception ex) {
                    throw new UserNotFoundException(ex);
                }
            } else {
                throw new UserNotFoundException();
            }
        }

        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private LdapTemplate buildTemplate(String username, String password) {
        LdapContextSource context = new LdapContextSource();
        context.setUrls(urls);
        context.setBase(base);
        context.setUserDn(format("%s@%s", username, domain));
        context.setPassword(password);
        context.setReferral("follow");
        context.afterPropertiesSet();
        return new LdapTemplate(context);
    }
}
