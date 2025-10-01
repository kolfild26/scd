package ru.sportmaster.scd.service.view.io;

import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;
import ru.sportmaster.scd.dto.view.ViewValidateFileResponseDto;
import ru.sportmaster.scd.ui.view.type.UiViewCrudRequest;

public interface ViewExcelReader {
    List<UiViewCrudRequest> readToChangeRequests(
        String viewName, String uuid, InputStream stream, Consumer<ViewValidateFileResponseDto> updateStatus
    );
}
