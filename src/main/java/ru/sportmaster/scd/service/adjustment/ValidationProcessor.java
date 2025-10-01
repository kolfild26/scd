package ru.sportmaster.scd.service.adjustment;

import org.springframework.web.multipart.MultipartFile;
import ru.sportmaster.scd.dto.adjustment.AdjustmentValidationRequestDto;
import ru.sportmaster.scd.dto.view.ViewValidateFileResponseDto;
import ru.sportmaster.scd.entity.adjustment.AdjustmentType;

public interface ValidationProcessor {
    String startValidateTask(AdjustmentValidationRequestDto request, MultipartFile file);

    ViewValidateFileResponseDto getValidationInfo(String uuid);

    void clearValidationInfo(String uuid);
}
