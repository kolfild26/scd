package ru.sportmaster.scd.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import ru.sportmaster.scd.dto.InfoDto;

@Service
@RequiredArgsConstructor
public class EnvServiceImpl implements EnvService {
    private final Environment environment;

    @Override
    public InfoDto getInfo() {
        return InfoDto.builder()
            .version("0.0.1")
            .build("1")
            .env(getProfile())
            .build();
    }

    private String getProfile() {
        return environment.getActiveProfiles()[0].toUpperCase();
    }
}
