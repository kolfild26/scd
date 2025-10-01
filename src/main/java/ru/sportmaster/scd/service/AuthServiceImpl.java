package ru.sportmaster.scd.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.sportmaster.scd.dto.LoginRequestDto;
import ru.sportmaster.scd.dto.UserDto;
import ru.sportmaster.scd.entity.User;
import ru.sportmaster.scd.security.JwtTokenHandler;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenHandler jwtTokenHandler;

    @Override
    public String auth(LoginRequestDto request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
            request.getUsername(),
            request.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenHandler.generate(authentication);
    }

    @Override
    public UserDto getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return UserDto.builder()
            .id(user.getId())
            .login(user.getUsername())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .build();
    }
}
