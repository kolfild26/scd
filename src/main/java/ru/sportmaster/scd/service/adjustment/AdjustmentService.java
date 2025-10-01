package ru.sportmaster.scd.service.adjustment;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ru.sportmaster.scd.dto.adjustment.AdjustmentAddSpecificationRequestDto;
import ru.sportmaster.scd.dto.adjustment.AdjustmentDownloadRequestDto;
import ru.sportmaster.scd.dto.adjustment.AdjustmentValidationRequestDto;
import ru.sportmaster.scd.dto.view.ViewValidateFileResponseDto;
import ru.sportmaster.scd.ui.view.type.UiView;
import ru.sportmaster.scd.web.UserEnvironment;

public interface AdjustmentService {
    UiView getDocumentRowView(Long documentId);

    String validate(AdjustmentValidationRequestDto request, MultipartFile file);

    ViewValidateFileResponseDto getValidationStatus(String uuid);

    Resource download(AdjustmentDownloadRequestDto request);

    void approve(Long documentId, UserEnvironment environment);

    void cancel(Long documentId, UserEnvironment environment);

    void delete(Long documentId, UserEnvironment environment);

    Long addSpecification(AdjustmentAddSpecificationRequestDto request, UserEnvironment environment);
}
