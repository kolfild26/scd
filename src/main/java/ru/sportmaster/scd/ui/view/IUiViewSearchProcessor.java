package ru.sportmaster.scd.ui.view;

import ru.sportmaster.scd.dto.view.UiViewSearchRequestDto;
import ru.sportmaster.scd.dto.view.UiViewSearchResponseDto;

public interface IUiViewSearchProcessor {
    UiViewSearchResponseDto pageSearch(UiViewSearchRequestDto request, Long userId);

    int getSearchItemPosition(String view, Long userId, int currentPosition, Byte direction);

    void clearSearch(String view, Long userId);
}
