package ru.sportmaster.scd.security;

import static ru.sportmaster.scd.utils.StreamUtils.readBytesToString;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenHandler {
    private final String secret;
    @Value("${scd.auth.expirationDays}")
    private Integer expirationDays;

    public JwtTokenHandler(@Value("${scd.auth.secret}") String secretPath) throws IOException {
        this.secret = readBytesToString(secretPath);
    }

    public String generate(Authentication authentication) {
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        return Jwts.builder()
            .setSubject(principal.getUsername())
            .setExpiration(Date.valueOf(LocalDate.now().plusDays(expirationDays)))
            .signWith(SignatureAlgorithm.HS512, secret)
            .compact();
    }

    public boolean validate(String jwt) {
        try {
            Jwts.parser().setSigningKey(secret).parse(jwt);
            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return false;
    }

    public String getUserNameFromToken(String jwt) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(jwt).getBody().getSubject();
    }
}
