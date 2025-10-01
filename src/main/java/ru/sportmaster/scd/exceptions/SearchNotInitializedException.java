package ru.sportmaster.scd.exceptions;

import org.springframework.lang.NonNull;
import ru.sportmaster.scd.dto.view.UiViewSearchRequestDto;

public class SearchNotInitializedException extends RuntimeException {
    private final UiViewSearchRequestDto request;

    public SearchNotInitializedException(@NonNull UiViewSearchRequestDto request) {
        this.request = request;
    }

    @Override
    public String getMessage() {
        return String.format("Система поиска не проинициализирована для запроса %s", request);
    }
}
