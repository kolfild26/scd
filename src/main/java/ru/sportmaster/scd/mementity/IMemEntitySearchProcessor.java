package ru.sportmaster.scd.mementity;

import ru.sportmaster.scd.dto.view.UiViewSearchRequestDto;
import ru.sportmaster.scd.dto.view.UiViewSearchResponseDto;

public interface IMemEntitySearchProcessor {
    UiViewSearchResponseDto pageSearch(UiViewSearchRequestDto request);

    int getSearchItemPosition(String sessionUuid, int currentPosition, Byte direction);

    void clearSearch(String sessionUuid);
}
