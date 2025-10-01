package ru.sportmaster.scd.service.adjustment;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.sportmaster.scd.dto.Status;
import ru.sportmaster.scd.dto.adjustment.AdjustmentAddSpecificationRequestDto;
import ru.sportmaster.scd.dto.adjustment.AdjustmentDownloadRequestDto;
import ru.sportmaster.scd.dto.adjustment.AdjustmentValidationRequestDto;
import ru.sportmaster.scd.dto.view.ViewValidateFileResponseDto;
import ru.sportmaster.scd.entity.adjustment.AdjustmentDoc;
import ru.sportmaster.scd.entity.adjustment.AdjustmentDocType;
import ru.sportmaster.scd.repository.adjustment.AdjustmentDocRepository;
import ru.sportmaster.scd.repository.adjustment.AdjustmentDocRowRepository;
import ru.sportmaster.scd.repository.adjustment.AdjustmentDocTypeRepository;
import ru.sportmaster.scd.repository.adjustment.AdjustmentUserRepository;
import ru.sportmaster.scd.service.adjustment.io.AdjustmentDocumentReader;
import ru.sportmaster.scd.service.adjustment.io.AdjustmentDocumentWriter;
import ru.sportmaster.scd.ui.view.UiViewManager;
import ru.sportmaster.scd.ui.view.type.UiView;
import ru.sportmaster.scd.web.UserEnvironment;

@Service
@RequiredArgsConstructor
public class AdjustmentServiceImpl implements AdjustmentService {
    private final UiViewManager viewManager;
    private final AdjustmentDocumentReader adjustmentDocumentReader;
    private final ValidationProcessor validationProcessor;
    private final AdjustmentDocRepository adjustmentDocRepository;
    private final AdjustmentDocRowRepository adjustmentDocRowRepository;
    private final AdjustmentDocTypeRepository adjustmentDocTypeRepository;
    private final AdjustmentDocumentWriter adjustmentTemplateBuilder;
    private final AdjustmentUserRepository adjustmentUserRepository;


    @Override
    public UiView getDocumentRowView(Long documentId) {
        AdjustmentDoc document = adjustmentDocRepository.findById(documentId);
        if (document == null) {
            return null;
        }
        AdjustmentDocType docType = document.getType();
        return viewManager.getView(docType.getType().getRowClass());
    }

    @Override
    public String validate(AdjustmentValidationRequestDto request, MultipartFile file) {
        return validationProcessor.startValidateTask(request, file);
    }

    @Override
    public ViewValidateFileResponseDto getValidationStatus(String uuid) {
        var status = validationProcessor.getValidationInfo(uuid);
        if (status != null && Status.DONE == status.getStatus()) {
            validationProcessor.clearValidationInfo(uuid);
        }
        return status;
    }

    @Override
    @Transactional
    public Resource download(AdjustmentDownloadRequestDto request) {
        if (request.isTemplate()) {
            AdjustmentDocType docType = adjustmentDocTypeRepository.findById(request.getTypeId());
            return new ByteArrayResource(adjustmentTemplateBuilder.buildTemplate(docType.getType()));
        } else {
            AdjustmentDoc document = adjustmentDocRepository.findById(request.getDocumentId());
            AdjustmentDocType docType = document.getType();
            List<Object> rows = adjustmentDocRowRepository.getAllRowsByDocumentId(
                docType.getType().getRowClass(), request.getDocumentId()
            );
            return new ByteArrayResource(adjustmentTemplateBuilder.buildDocument(docType.getType(), rows));
        }
    }

    @Override
    public void approve(Long documentId, UserEnvironment environment) {
        var userId = adjustmentUserRepository.getDcUserId(environment.getUserId());
        adjustmentDocRepository.approve(documentId, userId);
    }

    @Override
    public void cancel(Long documentId, UserEnvironment environment) {
        var userId = adjustmentUserRepository.getDcUserId(environment.getUserId());
        adjustmentDocRepository.cancel(documentId, userId);
    }

    @Override
    public void delete(Long documentId, UserEnvironment environment) {
        var userId = adjustmentUserRepository.getDcUserId(environment.getUserId());
        adjustmentDocRepository.delete(documentId, userId);
    }

    @Override
    public Long addSpecification(AdjustmentAddSpecificationRequestDto request, UserEnvironment environment) {
        var type = adjustmentDocTypeRepository.findById(request.getTypeId()).getType();
        var userId = adjustmentUserRepository.getDcUserId(environment.getUserId());
        var rows = adjustmentDocumentReader.convertToEntities(type, request.getData()).stream()
            .map(type::mapToStruct)
            .toList();

        if (request.getDocumentId() != null) {
            return adjustmentDocRowRepository.update(request.getDocumentId(), rows);
        } else {
            return adjustmentDocRowRepository.create(
                request.getTypeId(), request.getComment(), request.getBusinessId(), userId, rows
            );
        }
    }
}
