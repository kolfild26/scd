package ru.sportmaster.scd.service.airflow;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Profile("prod | test")
public class AirflowWebService {
    @Value("${scd.airflow.base-url}")
    private String baseUrl;
    @Value("${scd.airflow.api_part}")
    private String api;
    @Value("${scd.airflow.version}")
    private String version;

    public WebClient getWebClient(@NonNull ExchangeFilterFunction filter) {
        return
            WebClient
                .builder()
                .baseUrl(baseUrl + api + version)
                .filter(filter)
                .build();
    }

    public WebClient getWebClient() {
        return
            WebClient
                .builder()
                .baseUrl(baseUrl + api + version)
                .build();
    }

    public WebClient getWebClientAuth() {
        return
            WebClient
                .builder()
                .baseUrl(baseUrl)
                .build();
    }
}
