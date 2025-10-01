package ru.sportmaster.scd.service.adjustment;

import static java.lang.Boolean.TRUE;
import static ru.sportmaster.scd.dto.Status.DONE;
import static ru.sportmaster.scd.dto.Status.ERROR;
import static ru.sportmaster.scd.utils.CollectionUtil.merge;

import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.sportmaster.scd.dto.adjustment.AdjustmentValidationErrorDto;
import ru.sportmaster.scd.dto.adjustment.AdjustmentValidationRequestDto;
import ru.sportmaster.scd.dto.view.ViewValidateFileResponseDto;
import ru.sportmaster.scd.entity.adjustment.AdjustmentDocType;
import ru.sportmaster.scd.entity.adjustment.AdjustmentType;
import ru.sportmaster.scd.repository.adjustment.AdjustmentDocTypeRepository;
import ru.sportmaster.scd.service.adjustment.io.AdjustmentDocumentReader;
import ru.sportmaster.scd.service.adjustment.validator.DocumentHeaderValidator;
import ru.sportmaster.scd.service.adjustment.validator.DocumentRowValidator;
import ru.sportmaster.scd.task.HazelCastComponent;
import ru.sportmaster.scd.ui.view.UiViewManager;
import ru.sportmaster.scd.ui.view.type.UiView;

@Slf4j
@Component
@RequiredArgsConstructor
public class ValidationProcessorImpl implements ValidationProcessor {
    private final ExecutorService executor = Executors.newWorkStealingPool();
    private final UiViewManager viewManager;
    private final DocumentHeaderValidator documentHeaderValidator;
    private final List<DocumentRowValidator> documentRowValidators;
    private final AdjustmentDocTypeRepository adjustmentDocTypeRepository;
    private final AdjustmentDocumentReader adjustmentDocumentReader;
    private final HazelCastComponent hazelCastComponent;

    private Map<String, ViewValidateFileResponseDto> validateMap;

    @PostConstruct
    public void init() {
        validateMap = hazelCastComponent.getViewFileUploadValidateMap();
    }

    @Override
    public String startValidateTask(AdjustmentValidationRequestDto request, MultipartFile file) {
        AdjustmentDocType docType = adjustmentDocTypeRepository.findById(request.getTypeId());
        AdjustmentType type = docType.getType();

        UiView view = viewManager.getView(type.getRowClass());
        String uuid = UUID.randomUUID().toString();
        validateMap.put(uuid, ViewValidateFileResponseDto.progress(0f, 0));

        try {
            var data = file != null && !file.isEmpty()
                ? adjustmentDocumentReader.convert(docType.getType(), file)
                : request.getData();

            executor.execute(() -> {
                try {
                    List<AdjustmentValidationErrorDto> errors = new ArrayList<>();
                    if (request.getErrors() != null) {
                        request.getErrors().forEach(error -> {
                            error.setCorrected(true);
                            errors.add(error);
                        });
                    }

                    ViewValidateFileResponseDto info = validate(data, type, view, uuid, errors);
                    info.setProgress(100f);
                    info.setErrors(errors);
                    info.setData(data);

                    if (TRUE.equals(ERROR != info.getStatus() && !info.getHasBlockedErrors()) && info.isValid()) {
                        info.setStatus(DONE);
                    } else {
                        info.setStatus(ERROR);
                    }

                    validateMap.replace(uuid, info);
                    log.info("Validation task {} completed!", uuid);
                } catch (Exception ex) {
                    log.error(ex.getMessage(), ex);
                    var info = validateMap.get(uuid);
                    info.setStatus(ERROR);
                    validateMap.replace(uuid, info);
                }
            });
        } catch (Exception ex) {
            validateMap.replace(uuid, ViewValidateFileResponseDto.blockedError(ex.getMessage()));
        }

        return uuid;
    }

    private ViewValidateFileResponseDto validate(List<ObjectNode> data, AdjustmentType type, UiView view, String uuid,
                                             List<AdjustmentValidationErrorDto> errors) {
        ViewValidateFileResponseDto info = validateMap.get(uuid);
        log.info("Validation task {} started!", uuid);

        List<DocumentRowValidator> rowValidators = documentRowValidators.stream()
            .filter(validator -> validator.isSupported(type))
            .toList();

        for (int i = 0; i < data.size(); i++) {
            ObjectNode row = data.get(i);

            if (i == 0) {
                var headerErrors = documentHeaderValidator.validate(view, row, i);
                if (!headerErrors.isEmpty()) {
                    merge(errors, headerErrors);
                    info.setHasBlockedErrors(true);
                    info.setBlockedErrorText("Ошибка: неверный формат. Число полей не совпадает с типом view. "
                        + "Файл не может быть загружен."
                    );
                    break;
                }
            }

            for (DocumentRowValidator validator : rowValidators) {
                var rowErrors = validator.validate(view, row, i + 1L);
                merge(errors, rowErrors);
                if (!rowErrors.isEmpty()) {
                    info.setErrorsCount(info.getErrorsCount() + rowErrors.size());
                }
            }

            info.setProgress((float) i / data.size());
            validateMap.replace(uuid, info);
        }

        return info;
    }

    @Override
    public ViewValidateFileResponseDto getValidationInfo(String uuid) {
        return validateMap.get(uuid);
    }

    @Override
    public void clearValidationInfo(String uuid) {
        validateMap.remove(uuid);
    }
}
