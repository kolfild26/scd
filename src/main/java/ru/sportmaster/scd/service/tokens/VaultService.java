package ru.sportmaster.scd.service.tokens;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class VaultService {
    @Value("${scd.token.vault-uri}")
    private String baseUrl;

    public WebClient getWebClient() {
        return
            WebClient
                .builder()
                .baseUrl(baseUrl)
                .build();
    }

    public String loadTokenFromFile(@NonNull String fileName) throws IOException {
        return Files.readString(Paths.get(fileName), StandardCharsets.UTF_8);
    }

    public void saveTokenToFile(@NonNull String fileName,
                                @NonNull String token) throws IOException {
        Files.writeString(Paths.get(fileName), token);
    }
}
