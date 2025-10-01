package ru.sportmaster.scd.service.view;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ru.sportmaster.scd.dto.view.ViewDownloadRequestDto;
import ru.sportmaster.scd.dto.view.ViewValidateFileResponseDto;

public interface ViewFileService {
    Resource download(ViewDownloadRequestDto request, String sessionUuid, String formUuid);

    String startValidate(String viewName, MultipartFile file);

    ViewValidateFileResponseDto validateStatus(String uuid);

    void saveFileData(String uuid);
}
